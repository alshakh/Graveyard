var glSetup = {
    FOV : 45,
    dim : undefined,
    gl : undefined,
    canvas : undefined,
    projectionMatrix : mat4.create(),
    modelViewMatrix : mat4.create(),
    shaderProgs : [],
    objects : [],
    addObject : function(obj) { 
        glSetup.objects.push(obj);
    },
    light : {
        position : [0,0,0],
        specular : [1,1,1,1],
        ambient : [0.3,0.3,0.3,1],
        diffuse : [1,1,1,1]
    },
    init : function (canvasID, dim) {
        //  Init canvas
        var initCanvas = function(){
            var canvas = document.getElementById(canvasID);
            canvas.width = Math.min(window.innerWidth,window.innerHeight)-10;
            canvas.height = canvas.width;
            canvas.addEventListener('click', glSetup.rotate, false);
            return canvas;
        }
        var canvas = glSetup.canvas = initCanvas();
        //  Init WebGL
        var initWebGL = function() {
            var gl;
            if (!window.WebGLRenderingContext) {
                alert("Your browser does not support WebGL. See http://get.webgl.org");
                return;
            }
            try{
                gl = canvas.getContext("experimental-webgl");
            } catch(e) {
                console.error(e);
            }

            if (!gl) {
                alert("Can't get WebGL");
                return;
            }

            //  Set viewport to entire canvas
            gl.viewport(0,0,canvas.width,canvas.height);
            gl.enable(gl.DEPTH_TEST);
            gl.clearColor(0.8,0.8,0.8,1);
            return gl;
        }
        glSetup.gl = initWebGL();
        // dim
        glSetup.dim = dim;
        // matrices
        glSetup.resetMatrices();
    },
    resetMatrices : function() {
        // projection matrix
        glSetup.project(glSetup.FOV, 1/* square */, glSetup.dim);
        // ModelViewMatrix
        mat4.translate(glSetup.modelViewMatrix, glSetup.modelViewMatrix, [0,0,-glSetup.dim]);// TMP
    },
    changeDim : function() {
        // project
        // ?? modelViewMatrix
    },
    addShaderProg : function(vertID, fragID) {
        /*
           Adds the shader into shaderProgs and returns the index in it.
           */
        //  Compile a shader
        var compileShaderProg = function(gl,vert,frag) { 
            var compileAShader = function (gl,id)
            {
                //  Get shader by id
                var src = document.getElementById(id);
                //  Create shader based on type setting
                var shader;
                if (src.type == "x-shader/x-fragment")
                    shader = gl.createShader(gl.FRAGMENT_SHADER);
                else if (src.type == "x-shader/x-vertex")
                    shader = gl.createShader(gl.VERTEX_SHADER);
                else
                    return null;
                //  Read source into str
                var str = "";
                var k = src.firstChild;
                while (k)
                    {
                        if (k.nodeType == 3) str += k.textContent;
                        k = k.nextSibling;
                    }
                    gl.shaderSource(shader, str);
                    //  Compile the shader
                    gl.compileShader(shader);
                    //  Check for errors
                    if (gl.getShaderParameter(shader, gl.COMPILE_STATUS) == 0)
                        alert(gl.getShaderInfoLog(shader));
                    //  Return shader
                    return shader;
            };
            //  Compile the program
            var prog  = gl.createProgram();
            gl.attachShader(prog , compileAShader(gl,vert));
            gl.attachShader(prog , compileAShader(gl,frag));
            gl.linkProgram(prog);
            //  Check for errors
            if (gl.getProgramParameter(prog, gl.LINK_STATUS) == 0)
                alert(gl.getProgramInfoLog(prog));
            //  Return program
            return prog;
        }
        var thisProg = compileShaderProg(glSetup.gl, vertID, fragID);
        return glSetup.shaderProgs.push(thisProg) - 1;
    },
    project : function(fov, asp, dim) {
        mat4.identity(glSetup.projectionMatrix);
        mat4.perspective(glSetup.projectionMatrix, fov, asp, dim/16, 16*dim);
        //mat4.ortho(glSetup.projectionMatrix,-2.5,+2.5,-2.5,+2.5,-2.5,+2.5);
    },
    createObject : function (vertVecArr, rgbVecArr, normalVecArr, shaderProgI) {
        var gl = glSetup.gl;
        var vecArrToArr = function ( vecArr ) {
            var a = [];
            for( var i = 0 ; i < vecArr.length ; i++ ) {
                a.push(vecArr[i][0]);
                a.push(vecArr[i][1]);
                a.push(vecArr[i][2]);
            }
            return a;
        };
        var makeBuffer = function(array) {
            var buffer = gl.createBuffer();
            gl.bindBuffer(gl.ARRAY_BUFFER,buffer);
            gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(array),gl.STATIC_DRAW);
            return buffer;
        };
        return {
            no : vertVecArr.length,
            vertBuffer : makeBuffer(vecArrToArr(vertVecArr)) ,
            rgbBuffer : makeBuffer(vecArrToArr(rgbVecArr)) ,
            normalBuffer : makeBuffer(vecArrToArr(normalVecArr)) ,
            transformationMatrix : mat4.create(),
            shaderProgIdx : shaderProgI,
            material : { // todo
                specular : [1,1,1,1],
                emission : [0,0,0,1],
                ambient : [0,0,0,1],
                diffuse : [0.3,0.4,0.7,1],
                shininess : 16
            }
        }
    },
    rotate : function() {
        var m = glSetup.modelViewMatrix;
        mat4.rotate(m,m,0.3,[1,1,1]);
        glSetup.display();
    },
    display : function() {
        var draw = function(object) {
            var shaderProgIdx = object.shaderProgIdx;
            var gl = glSetup.gl;

             var modelViewMatrix = mat4.create();
             mat4.mul(modelViewMatrix, glSetup.modelViewMatrix,object.transformationMatrix);

            //  use Shader program
            var shaderProg = glSetup.shaderProgs[shaderProgIdx];
            gl.useProgram(shaderProg);

            gl.clear(gl.COLOR_BUFFER_BIT|gl.DEPTH_BUFFER_BIT);

            var normalMatrix = mat3.create();
            mat3.normalFromMat4(normalMatrix, modelViewMatrix);

            console.log(normalMatrix);

            //  Set projection and modelview matrixes
            gl.uniformMatrix4fv(gl.getUniformLocation(shaderProg,"projectionMatrix") , false , new Float32Array(glSetup.projectionMatrix));
            gl.uniformMatrix4fv(gl.getUniformLocation(shaderProg,"modelViewMatrix")  , false , new Float32Array(modelViewMatrix));
            gl.uniformMatrix3fv(gl.getUniformLocation(shaderProg,"normalMatrix")  , false , new Float32Array(normalMatrix));

            gl.uniform3fv(gl.getUniformLocation(shaderProg,"lightPos")  , new Float32Array(glSetup.light.position));
            gl.uniform4fv(gl.getUniformLocation(shaderProg,"lightSpecular")  ,  new Float32Array(glSetup.light.specular));
            gl.uniform4fv(gl.getUniformLocation(shaderProg,"lightAmbient")  , new Float32Array(glSetup.light.ambient));
            gl.uniform4fv(gl.getUniformLocation(shaderProg,"lightDiffuse")  , new Float32Array(glSetup.light.diffuse));

            gl.uniform4fv(gl.getUniformLocation(shaderProg,"materialEmission")  , new Float32Array(obj.material.emission));
            gl.uniform4fv(gl.getUniformLocation(shaderProg,"materialAmbient")   , new Float32Array(obj.material.ambient));
            gl.uniform4fv(gl.getUniformLocation(shaderProg,"materialSpecular")   , new Float32Array(obj.material.specular));
            gl.uniform4fv(gl.getUniformLocation(shaderProg,"materialDiffuse")   , new Float32Array(obj.material.diffuse));
            gl.uniform1f(gl.getUniformLocation(shaderProg,"materialShininess") , obj.material.shininess);


            var enableAttrib = function( attribName, buffer ) {
                gl.bindBuffer(gl.ARRAY_BUFFER,buffer);
                var attribLoc = gl.getAttribLocation(shaderProg,attribName);
                gl.enableVertexAttribArray(attribLoc);
                gl.vertexAttribPointer(attribLoc,3,gl.FLOAT,false,0,0);
                return attribLoc;
            }

            var attributes = [];
            attributes.push(enableAttrib ( "point", object.vertBuffer ))
            attributes.push(enableAttrib ( "rgb", object.rgbBuffer ))
            attributes.push(enableAttrib ( "normal", object.rgbBuffer ))

            //  Draw all vertexes
            gl.drawArrays(gl.TRIANGLES,0,object.no);

            //  Disable vertex arrays
            var disableAllAttributes = function() {
                for ( var i = 0 ; i < attributes.length ; i++ ){
                    gl.disableVertexAttribArray(attributes[i]);
                }
            }
            disableAllAttributes();

            //  Flush
            gl.flush ();

            //disable shader Prog
            //gl.useProgram(0);
        }

        for ( var i = 0 ; i < glSetup.objects.length ; i++ ) {
            draw(glSetup.objects[i]);
        }
    }
}

glUtils = {
    toRadians : function ( deg ) {
        return deg * Math.PI / 180;
    },
    sin : function( deg ) {
        return Math.sin(glUtils.toRadians(deg));
    },
    cos : function( deg ) {
        return Math.cos(glUtils.toRadians(deg));
    },
    sphere : function( r , inc ) {
        // return object { points : [], normals : [] }
        if ( inc === undefined ) inc = 5;

        var s2c = function(phi, theta) {
            return [
                glUtils.cos(phi)*glUtils.sin(theta),
                glUtils.sin(phi)*glUtils.sin(theta),
                                 glUtils.cos(theta)
            ];
        };


        var sphere = {};
        var points = sphere.points = [];
        var normals = sphere.normals = [];
        for ( var theta = 0 ; theta < 180 ; theta+=inc ) {
            for ( var phi = 0 ; phi < 360 ; phi += inc ) {
                p0 = s2c(phi       , theta );
                p1 = s2c(phi + inc , theta );
                p2 = s2c(phi       , theta + inc );
                p3 = s2c(phi + inc , theta + inc );

                points.push(p0,p1,p2);
                points.push(p1,p2,p3);
                normals.push(p0,p1,p2);
                normals.push(p1,p2,p3);
            }
        }

        return sphere;
    },
    cube : function() {
        var cube = {};
        var points = cube.points = [];
        var normals = cube.normals = [];
        var tmp =
            [
            -1,-1, 1, +1,-1, 1, -1,+1, 1,    -1,+1, 1, +1,-1, 1, +1,+1, 1,
            +1,-1,-1, -1,-1,-1, +1,+1,-1,    +1,+1,-1, -1,-1,-1, -1,+1,-1,
            +1,-1,+1, +1,-1,-1, +1,+1,+1,    +1,+1,+1, +1,-1,-1, +1,+1,-1,
            -1,-1,-1, -1,-1,+1, -1,+1,-1,    -1,+1,-1, -1,-1,+1, -1,+1,+1,
            -1,+1,+1, +1,+1,+1, -1,+1,-1,    -1,+1,-1, +1,+1,+1, +1,+1,-1,
            -1,-1,-1, +1,-1,-1, -1,-1,+1,    -1,-1,+1, +1,-1,-1, +1,-1,+1,
        ];
        for ( var i = 0 ; i < tmp.length ; i+=3 ) { 
            points.push([tmp[i],tmp[i+1],tmp[i+2]]);
        };

        normals = [
            [0,0,1],[0,0,1],[0,0,1],[0,0,1],[0,0,1],[0,0,1],
            [0,0,-1],[0,0,-1],[0,0,-1],[0,0,-1],[0,0,-1],[0,0,-1],
            [+1,0,0],[+1,0,0],[+1,0,0],[+1,0,0],[+1,0,0],[+1,0,0],
            [-1,0,0],[-1,0,0],[-1,0,0],[-1,0,0],[-1,0,0],[-1,0,0],
            [0,+1,0],[0,+1,0],[0,+1,0],[0,+1,0],[0,+1,0],[0,+1,0],
            [0,-1,0],[0,-1,0],[0,-1,0],[0,-1,0],[0,-1,0],[0,-1,0],
        ]
        return cube;
    }
}
