/*-----------------------------------------------------------------------------
 *              UNIVLMM - Universal LIBOR Market Model Add-in
 *                                Version 8.3
 *
 *       ************************** NOTICE *************************
 *                PROPRIETARY AND CONFIDENTIAL MATERIAL.
 *         DISTRIBUTION, USE, AND DISCLOSURE RESTRICTED BY LICENSE.
 *            (c) Copyright MBRM - MB Risk Management 1988-2001
 *               an FSS - Financial Systems Software Company
 *                (c) Copyright Dr Mamdouh Barakat 1988-2001
 *                           ALL RIGHTS RESERVED.
 *       ************************** NOTICE *************************
 *
 *  File : UNIVLMM.H
 *
    <<< For an example C / C++ file, please see supplied sample file : MBRMSAMP.CPP >>>


    The recommended steps to call our add-ins from C / C++ are :  	

    (1) Install the Universal Add-ins on your machine. Once the licence number is entered 
	    in Excel, you do not need to worry about the licence number from C or C++. 

		Alternatively, the licence number can be entered in C / C++ by calling functions :
				long   UYA_GETMACHINEID( void );			// Returns the Machine id
				double UYA_SETLICENCE( double LicenceNo );	// Enters licence number and returns
															// no. of days licence valid for.
		(if UNIVYLD is not licensed, similar functions are implemented in all other add-ins)

    (2)	In your C / C++ code, include the add-in's C / C++ header file(s).  
	    For UNIVYLD, the file is UNIVYLD.H , i.e. :
					#include "UNIVYLD.H"

    (3)	Examine the above C header file (e.g. UNIVYLD.H) to see the exact function definitions.

    (4)	Add the library file (for UNIVYLD, it is UNIVYLD.LIB) to your C / C++ project 
		(you could locate the file in the installation directory, e.g. C:\MBRM).  
		The exact way to add library files depends on the C / C++ compiler used.  
		In Microsoft Visual C / C++, you add the library file in the workspace screen 
		(in the section which lists  the .C / .CPP project files).

    (5)	Ensure any referenced header files (e.g. UNIVYLD.H) can be located by your compiler 
		(e.g. by copying them into your project's directory).

    (6)	If using arrays, please don't change the C / C++ compiler's default 
		Structure Member Alignment (which should, by default, be set to 8 Bytes).  

		This option is accessed via Microsoft Visual C++ menu : 
			Project > Settings > C / C++ > Code Generation.

 ----------------------------------------------------------------------------*/

#ifdef __cplusplus
extern "C" {            /* Assume C declarations for C++ */
#endif  /* __cplusplus */

#ifndef _INC_WINDOWS
#include <windows.h>
#endif


#ifndef FPARRAY_SIZE

#define FPARRAY_SIZE 10000

  // The above sets the size of the array in FPARRAY (see below) to the largest array size 
  // you require [you can increase this number by replacing 10000 with a larger size].  
  // Alternatively, for dynamic memory allocation of arrays, please see 
  // MBRM's online support / discussion forums (at http://www.mbrm.com/asp) */

typedef struct {
    unsigned short 	iRows,
	    			iCols;
    double  		dArray[FPARRAY_SIZE];
} FPARRAY;

typedef FPARRAY *LPFP;

//  FPARRAY is the structure which holds an Array of doubles.
//  A pointer (i.e. type LPFP) to this structure is passed to a function in a DLL 
//  when the argument being passed is an array of doubles.  Similarly, when a DLL function  
//	returns an array, it actually returns an pointer (type LPFP) to the structure FPARRAY.
//  The first two fields, <iRows> and <iCols>, define the number of rows and columns, 
//  respectively, in the array.  Field <dArray[0]> is the first element of the array. 
//  The remaining elements of the array (if any) follow consecutively.
//
//  With two dimensional arrays, the elements of the first row is stored first, 
//  followed by all the elements of the second row etc.
//
//  i.e. for a 3 column array, the storage structure is (assuming offset 0 for ".darray[ ]") :
//			array_name.dArray[0] = row 1, column 1 value
//  		array_name.dArray[1] = row 1, column 2 value
//  		array_name.dArray[2] = row 1, column 3 value
//  		array_name.dArray[3] = row 2, column 1 value
//  		array_name.dArray[4] = row 2, column 2 value
//  		array_name.dArray[5] = row 2, column 3 value
//  		array_name.dArray[6] = row 3, column 1 value
//  			etc.

typedef FPARRAY far *LPFP;

#endif



long WINAPI ULA_GETVERSION( void );
	 /* Gets version number for add-in */

long WINAPI ULA_GETMACHINEID( void );
	/* Gets computer id */

double WINAPI ULA_GETLICENCE( void );
        /* Returns the licence number entered for UNIVLMM.  
        This is associated with the machine id in ULA_GETMACHINEID( )*/

long WINAPI ULA_CHECK_INTEGRITY( long Parameter );

double WINAPI ULA_SETLICENCE( double LicenceNo );
	/* Enters licence number. Returns no of days to go */

double WINAPI ULA_GETDAYSTOEXPIRY( void );
		/* Returns no of days before licence expires */

long WINAPI ULA_DEINSTALL( void );
	/* de-installs licence  */

long WINAPI ULA_MULTITHREAD( long FLAG );

long WINAPI ULA_FREE(LPFP Pointer);

int WINAPI SET_MULTITHREAD(int FLAG);

int WINAPI FREE_LPFP(LPFP pointer);

char* WINAPI ULA_STATUS_NAME(double any_double_in_output);

char* WINAPI ULA_GET_ERRORLOG(int i);

LPFP WINAPI ULA_GET_SUBMATRIX(LPFP A, int iRow0, int iCol0, int iRow1, int iCol1);

LPFP WINAPI ULA_PADZEROS(LPFP A, int no_of_rows);

char* WINAPI ULA_SETUP_YIELD(char *reference, LPFP R, int Rtype,
                             double SpotDate);

char* WINAPI ULA_SETUP_YIELD2(LPFP Curve, short ITech, short ICurve,
                              double SpotDate, char *reference);

double WINAPI ULA_GET_FORWARD_LIBOR(double t1, double t2, double daysbasis,
                                    char *reference);

char* WINAPI ULA_FREE_YIELD(char *reference);

char* WINAPI ULA_SETUP_VOLATILITY(LPFP V, LPFP F, double SpotDate,
                                  double compfreq, char *reference);

char* WINAPI ULA_SETUP_FACTORS(int NoOfDates, int NoOfFactors, LPFP begforw, LPFP Factors_Array,
                                   char *reference);

char* WINAPI ULA_FREE_VOLATILITY(char *reference);

char* WINAPI ULA_SETUP_SKEW_GRID(char *skewgridref, LPFP Moving_Skew, LPFP Fixed_Skew,
                                 double SpotDate);

char* WINAPI ULA_FREE_SKEW_GRID(char *reference);

LPFP WINAPI ULA_CAPS_AND_SWAPTIONS(int mode, char *instrumref, char *yieldref,
                                   char *lmmvolref, char *skewaddonref,
                                   double Vconst, LPFP exesch, LPFP amortiz,
                                   double SpotDate, double daysbasis,
                                   double nominal, double blavolprice,
                                   char *order, int noofsteps, int noofsims,
                                   int seed);

char* WINAPI ULA_FREE_INSTRUMENT(char *reference);

LPFP WINAPI ULA_CAPCOLFLR(char *InstrumRef, char *YieldRef, LPFP Curve,
                          short iTech, short iCurve, char *LMMVolRef,
                          double Vol, char *SkewAddonRef, short OptionType,
                          double LowerStrike, double UpperStrike,
                          double DateFrom, double DateTo, short CPA,
                          short BusRule, short HolRule, LPFP Hols,
                          short AddifFixingToday, double DaysToSpot,
                          short FlagSpotToSpot, double TodaysDate,
                          double DaysBasis, double Nominal,
                          LPFP Amortization, double FXvsBasis,
                          int NoOfSteps, int NoOfSims, int Seed);

#ifndef OPERnum

typedef struct _oper {
    union {
        double  num;
        unsigned char  *str;
        unsigned short flag;
        unsigned short err;
        //
        struct {
            struct _oper *lparray;
            unsigned short rows;
            unsigned short cols;
        } array;
    } val;
    //
    unsigned short type;
} OPER;
typedef OPER *pOPER;

#define OPERnum   1
#define OPERstr   2
#define OPERbool  4
#define OPERerr   16
#define OPERarray 64
#define OPERmiss  128
#define OPERempty 256

#endif


LPFP WINAPI ULA_CALIBRA(char *yieldref, char *lmmvolref, double compfreq,
                        int nooffacs, char *skewaddonref, LPFP corvol,
                        pOPER refprice, LPFP caps, LPFP swaptions,
                        double swapfreq, LPFP weights, LPFP volguess,
                        LPFP corguess, double todaysdate, double daysbasis,
                        int volpoints, int mode, double tolvol,
                        double tolcor, int trials);

/* ULA_CALIBRA2( ) is similar ro ULA_CALIBRA( ) but takes in an LPFP structure for 
"refprice" array rather than a pOPER structure */

LPFP WINAPI ULA_CALIBRA2(char *yieldref, char *lmmvolref, double compfreq,
                         int nooffacs, char *skewaddonref, LPFP corvol,
                         LPFP refprice, LPFP caps, LPFP swaptions,
                         double swapfreq, LPFP weights, LPFP volguess,
                         LPFP corguess, double todaysdate, double daysbasis,
                         int volpoints, int mode, double tolvol,
                         double tolcor, int trials);

LPFP WINAPI ULA_CALIBRA_GET_VOLS(char *lmmvolref);

LPFP WINAPI ULA_CALIBRA_GET_TIME_DEPS(char *lmmvolref);

double WINAPI ULA_CALIBRA_GET_COMPFREQUENCY(char *lmmvolref);

LPFP WINAPI ULA_CALIBRA_GET_FACTORS(char *lmmvolref);

LPFP WINAPI ULA_CALIBRA_GET_FITTED_CORRELATIONS(char *lmmvolref);

LPFP WINAPI ULA_CALIBRA_GET_BEG_RATES(char *lmmvolref);

LPFP WINAPI ULA_MARKET_AND_FIT(char *lmmvolref, short mode);

LPFP WINAPI ULA_SWAPTION_GRID(char *yieldref, char *lmmvolref,
                              char *skewaddonref, double swapfreq,
                              LPFP expirypoints, LPFP termpoints,
                              double strike, short OptionClass,
                              double todaysdate, double daysbasis,
                              double nominal, short mode);

char* WINAPI ULA_SETUP_SIMULATION(char *yieldref, char *lmmvolref,
                                  char *skewaddonref, LPFP liborrates,
                                  double maxpossibletime, LPFP intersteps,
                                  LPFP discounttimes, double todaysdate,
                                  int actualnoofsteps, int mode,
                                  char *lmmsimref);

LPFP WINAPI ULA_SIMULATION(char *lmmsimref, int seed, short mode, double daysbasis);

char* WINAPI ULA_FREE_SIMULATION(char *reference);

LPFP WINAPI ULA_PRICE_AUTOFLEX_CAP(char *yieldref, char *lmmvolref,
                                   char *skewaddonref, LPFP exesch,
                                   LPFP amortiz, double SpotDate,
                                   double daysbasis, short mode,
                                   int Nautoflex, int noofsteps,
                                   int noofsims, int seed);

LPFP WINAPI ULA_PRICE_SPREAD_OPTION(char *yieldref, char *lmmvolref,
                                    double K, short optiontype, double Texpiry,
                                    double T1beg, double T1end, double compfreq1,
                                    double T2beg, double T2end, double compfreq2,
                                    short discountf, double Ttoday, double nominal,
                                    double daysbasis, int noofsteps, int noofsims,
                                    int seed);

LPFP WINAPI ULA_PRICE_TRIGGER_SWAP(char *yieldref, char *lmmvolref,
                                   char *skewaddonref, LPFP exesch,
                                   LPFP amortiz, double SpotDate,
                                   double daysbasis, double lowerlevel,
                                   double upperlevel, int noofsteps,
                                   int noofsims, int seed);

LPFP WINAPI ULA_PRICE_RESET_CAP(char *yieldref, char *lmmvolref,
                                char *skewaddonref, LPFP exesch, LPFP amortiz,
                                double SpotDate, double daysbasis,
                                LPFP resetschedule, int  noofsteps,
                                int noofsims, int seed);

LPFP WINAPI ULA_PRICE_BERMUDAN_SWAPTION(char *yieldref, char *lmmvolref,
                                        char *skewaddonref, LPFP exesch,
                                        double SpotDate, double daysbasis,
                                        int noofsteps, int noofsims,
                                        int seed, int mode, LPFP kinkypoints);

LPFP WINAPI ULA_PRICE_CASHFLOW(char *yieldref, char *lmmvolref, LPFP exesch,
                               LPFP cashflowsch, short usetimecoupf,
                               short typeofaccr, double couprate, int equalcoup,
                               short cpa, double SpotDate, int noofsteps,
                               int noofsims, int seed, LPFP kinkypoints, int mode);

LPFP WINAPI ULA_PRICE_CTD(char *yieldref, char *lmmvolref,
                          double deliverydate, double todaysdate,
                          LPFP bondsarray, short usetimecoupf, int noofsteps,
                          int noofsims, int seed);

char* WINAPI STATUS_NAME(double any_double_in_output);

char* WINAPI GET_ERRORLOG(int i);

LPFP WINAPI GET_SUBMATRIX(LPFP A, int iRow0, int iCol0, int iRow1, int iCol1);

/* CALCULATION OF VARIANCE/COVARIANCE AND CORRELATION MATRIXES.
   These can be used as inputs intp VaR and/or Principal Component Analysis  */
  
LPFP WINAPI ULA_CALC_VC(char *portfolioname, pOPER assetarray,
                               char *fromdate, char *todate, int mode,
                               double decay, double confidence,
                               double timescale, pOPER assetlist);

/* PRINCIPAL COMPONENT ANALYSIS */

LPFP WINAPI ULA_CALC_FACTORS(char *refPCA, LPFP cor, int s, double tolerance,
                             int maxnoofval, LPFP guess, int mode);

LPFP WINAPI ULA_GET_FACTORS(char *refPCA);


LPFP WINAPI ULA_GET_FITTED_CORRELATIONS(char *refPCA);

char* WINAPI ULA_FREE_PCA(char *refPCA);


#ifdef __cplusplus
}
#endif  /* __cplusplus */

/*
Comments :
------------------------------------------------------------------------------

double ULA_SETUP_YIELD( )
    assigns a certain term structure of interest rates to a certain slot,
    so that other functions can take in input the chosen slot number
    rather than all the parameter defining that term structure.
    "R" is the actual curve, "SpotDate" is today's date (zero if every time
    parameter is in years), "Rtype" is defined as in the UNIVDRV add-in and
    "ivolrat" is the slot number.

double ULA_SETUP_YIELD2( )
    Analogous to the previous function, but the term structure and relative
    parameters are defined in the UNIVSWAP add-in.

double ULA_SETUP_VOLATILITY( )
    assigns a certain term structure of volatilities to a certain slot.
    The array "tvvf" is a four column array: time, 1st factor, 2nd factor,
    time dependence (optional).  The whole array may consist of a single
    value volatility, or the time dependence may be omitted, or both the
    time dependence and the 2nd factor may be omitted.

double ULA_GET_FORWARD_RATE( )
    Once a certain term structure has been assigned to a certain slot,
    one may use this function to calculate the forward rate between time
    "t1" and time "t2".  If the basis "daysbasis" is zero, the continuous
    rate will be returned.

LPFP ULA_CAPS_AND_SWAPTIONS( )
    The "low-level" function for pricing caps/floors, swaptions and other
    common derivatives (collars, corridors, digitals, etc.).  With this
    function the user can specify the start and the end of each reset period,
    thus having a greater control upon the description of the option.
    Here are the input parameters:
      "iinstrum"
          A slot number relative to all the parameters of the option
          priced by this function (including term structures of interest
          rates and volatility).
      "exesched"
          The exercise schedule.  A six columns array: beginning of reset
          period, end of reset period, strike for a payer option, strike for
          a receiver option, option type (see below), factor of option payoff.
              Option type: 1 = cap
                           2 = floor
                           3 = collar
                           4 = corridor
                          11 = digital cap
                          12 = digital floor
                          13 = digital collar
                          14 = digital corridor
      "mode"
          allows choice between
              1 = caps/floors/corridors
              2 = upfront caps/floors/corridors
              3 = swaptions
      "volskew"
          A skew correction to the whole volatility term structure.
      "order"
          You can choose the order in which price and sensitivities are
          returned, these are the codes:
              "p" price
              "d" delta
              "g" gamma
              "t" theta
              "v" vega
          Each sensitivity is calculated by shifting the term structure
          and looking at the difference in price.  The amount of the shift
          can be controlled by specifying it as a number of basis points
          just before the code of the sensitivity.  Also, one may restrict
          this way of calculating the sensitivity to positive or negative
          shifts only by adding a "+" or a "-" before the number of basis
          respectively.  Examples of a valid "order" parameter: "pdg",
          "p 100dg", "p+50dgt 50v", etc.
      "ivolrat", "SpotDate" and "daysbasis"
          are defined as in the previous functions.

double ULA_FREE_YIELD_VOL( )
    frees the memory that had been assigned to the term structures of
    interest rates and volatility relative to the slot number "islot".

double ULA_FREE_INSTRUMENT( )
    frees the memory that had been assigned to all the parameters of
    the option specified by the slot number "islot".

LPFP ULA_CALIBRA( )
    The "low-level" function for the calibration.  Here is a description
    of the input parameters:
      "DP"
          Each row of this two-columns array contains the slot number of
          the instrument one whishes to calibrate and its observed market
          price.
      "points"
          Basically, the number of rows in the array that represents the
          calibrated term structure of volatility.  But, if the term
          structure is flat at the beginning, the point corresponding to
          time zero is not counted.
      "trials"
          The number of times the calibration procedure is run from a
          different initial guess of the term structure of volatility,
          including the one specified by the user.
      "guess"
          a term structure of volatility, as a starting point of the
          calibration procedure.
      "cor"
          A correlation matrix for the two factors.  It has the following
          format:
              time0
              time1 cor10
              time2 cor20 cor21
              time3 cor30 cor31 cor32
               ...   ...   ...   ...   ...
      "ivolrat"
          The place where the solution is stored.  But remind that the
          calibrated term structure of volatility is stored separately of
          any term structure specified directly by the user with the
          function USE2F_SETUP_VOLATILITY.
      "mode"
          specifies a set of different alternatives.  It has format edcba,
          with
              (a) The initial guess for the calibration: chosen by the user,
                  0, or stored in the slot number "ivolrat" when the last
                  calibration with that slot was done, 1, or internal
                  default, 2.  By the way, it affects only the first trial.
              (b) Whether the dependence on time of the term structure (the
                  fourth column of the array that represents the term
                  structure) has to be calibrated or not: yes, 0 or no, 1.
              (c) The calibration procedure needs the derivatives of the
                  objective function.  This parameter specifies how such
                  derivatives are calculated: right-left derivatives, 0,
                  right derivatives, 1, or left derivatives, 2.
              (d) Whether the term structure of the volatility has to
                  be calibrated from valuation time, 0, or from the beginning
                  of the first reset period, 1.
              (e) How the discrepancy between market and fitted prices (that
                  one wishes to minimize) is calculated: relative, 0, or
                  absolute, 1.
      "tol"
          The integer part of this number is the maximum number of calls
          to the pricing function that the calibration function is allowed
          to use.  A good calibration may be obtained with just a few
          hundreds calls to the pricing function, but we would advise to set
          this parameter to a much higher value (i.e., 10000, 20000, etc.).
          The fractional part is the required minimum discrepancy between
          the market prices and the fitted ones.  For a good calibration,
          set it to zero (so the optimization algorithm will stop when it
          has got convergence).

LPFP ULA_CALIRO_GET_LAST( )
      returns the volatility term structure resulting from the last
      calibration that has been done.

LPFP ULA_CALIRO_GET( )
    analogous to the previous function, but it refers to the calibration
    done with a certain slot number.

LPFP ULA_MARKET_AND_FIT( )
    returns an array of two columns and as many rows as the instruments
    one used for the calibration.  The first element of each row is
    the market price of the instrument and the second is the fitted price,
    showing the effectiveness of the calibration.

LPFP ULA_CAPCOLFLR( )
    The "high-level" function for pricing caps/floors, swaptions and the
    other common derivatives.  Here the parameter are specifies as in the
    analogous function of the UNIVSWAP add-in (apart the instrument number
    "iinstrum").

LPFP ULA_VOLCALIBRA( )
    The "high-level" calibration function.  Apart the two parameters
    "capfloors" and "swaptions" that describe the instruments one wishes
    to calibrate, all the other parameters either have been already
    described in this document or have the some meaning as in the UNIVSWAP
    add-in, and the user should refer to the documentation of that add-in.
      "capfloors"
          Each row of this array describes a couple cap-floor, and may
          consist of up to eight elements, though the last two are optional:
          in order, valuation time, end of the last caplet/floorlet, floor
          strike, cap strike, reset frequency per annum, Black volatility
          of the couple or, in alternative to the Black volatility, floor
          market price and cap market price.
      "swaptions"
          This is a volatility grid representing the Black volatilities of
          at-the-money swaptions.  The first column is the expiry of each
          option, while the first row is the underlying term.
      NB Every parameter of time in the above two arrays is in years.

char* ULA_STATUS_NAME( )
    a string telling the user why his input has not been accepted.

LPFP ULA_GET_COLUMN( )
    returns the column "iCol" of the array "A" arranged as an array made of
    "NoOfCols" columns.  Example:
            (1 2 3)
        A = (4 5 6)    iCol = 2    NoOfCols = 2    result = (2 5)
            (7 8 9)                                         (8 1)
            (0 1 2)
    Useful to show on the spreadsheet a comparison between market and fitted
    option prices.

------------------------------------------------------------------------------
*/
