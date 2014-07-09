/** 
	@authour Ahmed Alshakh alshakh.net
	@date june 9, 2014
*/
/** 
    How to use :
    1) use paramInit() in main once to initialize the paramDisplay
    2) add all parameteres by paramSet/paramInfoSet 
        paramSet if user is allowed to selected it, paramInfoSet if not
    3) Offer three funcions increase(), decrease() and get()
    4) Add paramKeyPressed() in your key function
    5) OPTIONAL : offer keys to call paramShow() and/or paramHide().
*/
/** Example 
  in main() 
    paramInit(5,get,inc,dec);
	paramSet(0,"point distance : %s","point distance : <%s>");
	paramInfoSet(1,"Angle 1 : %s");
	paramSet(2,"point size : %s","point size : <%s>");
	paramSet(3,"e|w to dis|en : %s" , "e|w to dis|en : <%s>");
	paramInfoSet(4,"Angle 2 : %s");
  in File 
    //! ///////////////////////////////
    //! /// ParamDisp    functions ////
    //! ///////////////////////////////
    //! ///////////////////////////////
    void inc(int i){//!
    	if(i==0) pointDistance++;
    	else if  (i==2) {
    		if(pointSize>=30)
    			pointSize=30;
    		else 
    			pointSize++;
    	}
    }
    void dec(int i){//!
    	if(i==0) pointDistance--;
    	else if (i==2) {
    		if(pointSize<=0)
			    pointSize=0;    
		    else
			    pointSize--;
	    }
    }
    #define INFO_LEN 100
    char inf[INFO_LEN];
    char* get(int i){
        //
	    if(i==0) sprintf(inf, "%d", pointDistance);
	    else if(i==1)  sprintf(inf, "%d", th);
	    else if(i==2) sprintf(inf, "%d", pointSize);
	    else if(i==3) sprintf(inf, "%s", (param3Disabled?"disabled":"enabled"));
	    else if(i==4) sprintf(inf, "%d", ph);
        
        //
        return inf;
    }
  in key() function 
    paramKeyPressed(ch);
**/
#ifndef paramDISP
#define paramDISP

void paramInit(int,char* (*)(int),void (*)(int),void (*)(int));
void paramSet(int,char*,char*);
void paramInfoSet(int,char*);
void paramPrint(const char* format , ...);
void paramKeyPressed(char);
void paramDraw();
void paramShow();
void paramHide();
void paramReset();
void paramDisable(int);
void paramEnable(int);
#endif
