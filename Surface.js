var Surface = {
    createSurface : function(eqn, dqn, x0, x1, y0,y1, xParts, yParts) {
        var zMin = undefined;
        var zMax = undefined;

        var xDim = (x1-x0)
        var yDim = (y1-y0)
        var xInc = xDim/xParts;
        var yInc = yDim/yParts;

        var tPoints = [];
        var tNormals = [];
        for ( var x = x0 ; x < x1 ; x+=xInc ) {
            for ( var y = y0 ; y < y1 ; y+=yInc ) {
                p0 = eqn(x     ,y);
                p1 = eqn(x+xInc,y);
                p2 = eqn(x     ,y+yInc);
                p3 = eqn(x+xInc  ,y+yInc);
                tPoints.push(p0,p1,p2);
                tPoints.push(p1,p2,p3);


                n0 = dqn(x       ,y);
                n1 = dqn(x+xInc  ,y);
                n2 = dqn(x       ,y+yInc);
                n3 = dqn(x+xInc  ,y+yInc);
                tNormals.push(p0,p1,p2);
                tNormals.push(p1,p2,p3);


                if ( zMin === undefined || zMin > p0[2] ) {
                    zMin = p0[2];
                }
                if ( zMax === undefined || zMax < p0[2] ) {
                    zMax = p0[2];
                }
            }
        }
        var tRgb = [];

        var zDim = zMax - zMin;

        var mapRgb = function(hue) {
            /**
             * Converts an HSV color value to RGB. Conversion formula
             * adapted from http://en.wikipedia.org/wiki/HSV_color_space.
             * Assumes h, s, and v are contained in the set [0, 1] and
             * returns r, g, and b in the set [0, 1].
             *
             * @param   Number  h       The hue
             * @param   Number  s       The saturation
             * @param   Number  v       The value
             * @return  Array           The RGB representation
             */
            var hsvToRgb = function(h, s, v){
                var r, g, b;

                var i = Math.floor(h * 6);
                var f = h * 6 - i;
                var p = v * (1 - s);
                var q = v * (1 - f * s);
                var t = v * (1 - (1 - f) * s);

                switch(i % 6){
                    case 0: r = v, g = t, b = p; break;
                    case 1: r = q, g = v, b = p; break;
                    case 2: r = p, g = v, b = t; break;
                    case 3: r = p, g = q, b = v; break;
                    case 4: r = t, g = p, b = v; break;
                    case 5: r = v, g = p, b = q; break;
                }

                return [r, g, b];
            }
            return hsvToRgb(hue,1,1)
        }
        for ( var i = 0 ; i < tPoints.length ; i++ ) {
            tRgb.push(mapRgb((tPoints[i][2]-zMin)/zDim));
        }

        console.log(tRgb);

        return {
            points : tPoints,
            normals : tNormals,
            rgb : tRgb
        }
    },
    createWireFrame : function(eqn, color, x0, x1, y0,y1, xParts, yParts) {
        var xDim = (x1-x0)
        var yDim = (y1-y0)
        var xInc = xDim/xParts;
        var yInc = yDim/yParts;

        var tPoints = [];
        for ( var x = x0 ; x < x1 ; x+=xInc ) {
            for ( var y = y0 ; y < y1 ; y+=yInc ) {
                p0 = eqn(x     ,y);
                p1 = eqn(x+xInc,y);
                p2 = eqn(x     ,y+yInc);
                p3 = eqn(x+xInc  ,y+yInc);
                tPoints.push(p0,p1);
                tPoints.push(p1,p2);
                tPoints.push(p2,p3);
                tPoints.push(p3,p0);
            }
        }
        var tRgb = [];
        var tNormals = [];
        for ( var i = 0 ; i < tPoints.length ; i++ ) {
            tRgb.push(color);
            tNormals.push([0,0,0]);// no normals for lines
        }

        return {
            points : tPoints,
            rgb : tRgb,
            normals : tNormals
        }
    }
}
