/*-----------------------------------------------------------------------------
 *                  UNIVDRV - Universal Derivatives Add-in
 *                               Version 8.2g
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
 *  File : UNIVDRV.H
 ----------------------------------------------------------------------------*/

#ifndef FPARRAY_SIZE
  #define FPARRAY_SIZE 100

  typedef struct {
    unsigned short iRows;
    unsigned short iCols;
    double         dArray[FPARRAY_SIZE];
  } FPARRAY;

  typedef FPARRAY far *LPFP;
#endif

#ifndef PLAIN
#define AMERICAN            -2
#define PLAIN               -1
#define ASIAN               0
#define SASIAN              1
#define DOWN_AND_IN         2
#define UP_AND_IN           3
#define DOWN_AND_OUT        4
#define UP_AND_OUT          5
#define LOOKBACK            7
#define COMPOUND            8
#define EURO_DIGITAL        12
#define UP_ONE_TOUCH        13
#define DOWN_ONE_TOUCH      14
#define TWO_TOUCH           15
#define DOUBLE_BARRIER_IN   16
#define DOUBLE_BARRIER_OUT  17
#define ASSET_OR_NOTHING    18
#define NO_TOUCH            19
#define XASIAN              20
#define SXASIAN             21
#define UP_NO_TOUCH         23
#define DOWN_NO_TOUCH       24
#define CTWO_TOUCH          25
#define CDOUBLE_BARRIER_IN  26
#define CDOUBLE_BARRIER_OUT 27
#define CNO_TOUCH           29
#define UP_ONE_TOUCHE       33
#define DOWN_ONE_TOUCHE     34
#define TWO_TOUCHE          35
#define WARRANT             40
#define WASIAN              100
#define WSASIAN             101
#define WDOWN_AND_IN        102
#define WUP_AND_IN          103
#define WDOWN_AND_OUT       104
#define WUP_AND_OUT         105
#define WLOOKBACK           107
#define WUP_ONE_TOUCH       113
#define WDOWN_ONE_TOUCH     114
#define WTWO_TOUCH          115
#define WDOUBLE_BARRIER_IN  116
#define WDOUBLE_BARRIER_OUT 117
#define WNO_TOUCH           119
#define WXASIAN             120
#define WSXASIAN            121
#define WUP_NO_TOUCH        123
#define WDOWN_NO_TOUCH      124
#define WUP_ONE_TOUCHE      133
#define WDOWN_ONE_TOUCHE    134
#define WTWO_TOUCHE         135
#endif

#ifdef __cplusplus
extern "C" {            /* Assume C declarations for C++ */
#endif  /* __cplusplus */

#ifndef _INC_WINDOWS
#include <windows.h>
#endif

/* note that the "2" functions, such as UDA_XARRAY2( ), are identical to the
   standard functions, such as UDA_XARRAY( ), except that the "2" functions
   are more powerful in that they allow the "External Force of Discount" to be
   an array or a single value.  The standard functions are still listed
   for backward compatibility if calling from a programming language.  
   
   In Excel, once UNIVDRV is attached, all calls to the standard functions 
   are automatically mapped to the new "2" functions */


LPFP	WINAPI UDA_XARRAY(	char *order, LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Strike_array, LPFP Vol_Array, double Expiry_Date, 
							double Ext_Force_Discount, short Class, short model,
							LPFP Exotic_Array, long NoSamplings, double Steps_price, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double Steps_time);

LPFP	WINAPI UDA_XARRAY2(	char *order, LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Strike_array, LPFP Vol_Array, double Expiry_Date, 
							LPFP Ext_Force_Disc_Array, short Class, short model,
							LPFP Exotic_Array, long NoSamplings, double Steps_price, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double Steps_time);

double WINAPI UDA_XIMPLIEDVOL(	LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Strike_array, double Opt_Price, double Expiry_Date, 
							double Ext_Force_Discount, short Class, short model,
							LPFP Exotic_Array, long NoSamplings, double Steps_price, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double Steps_time, double VolTol, 
							double InitialVolGuess);

double WINAPI UDA_XIMPLIEDVOL2(	LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Strike_array, double Opt_Price, double Expiry_Date, 
							LPFP Ext_Force_Disc_Array, short Class, short model,
							LPFP Exotic_Array, long NoSamplings, double Steps_price, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double Steps_time, double VolTol, 
							double InitialVolGuess);

double WINAPI UDA_XIMPLIEDVOL_ALT(	LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Strike_array, double Opt_Price, double Expiry_Date, 
							double Ext_Force_Discount, short Class, short model,
							LPFP Exotic_Array, long NoSamplings, double Steps_price, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double Steps_time, double VolTol, 
							double InitialVolGuess);

double WINAPI UDA_XIMPLIEDVOL_ALT2(	LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Strike_array, double Opt_Price, double Expiry_Date, 
							LPFP Ext_Force_Disc_Array, short Class, short model,
							LPFP Exotic_Array, long NoSamplings, double Steps_price, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double Steps_time, double VolTol, 
							double InitialVolGuess);

LPFP WINAPI UDA_SIMULATOR( LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Vol_Array, double Expiry_Date, double Ext_Force_Discount, 
							short model, long seed, double Window_start, double Window_end,
							long NoSamplings, long No_of_simulations, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double FractExpiryLastTradingDay, 
							short Return_Req);

LPFP WINAPI UDA_SIMULATOR2( LPFP R_Array, double Underlying_Inst_Px, 
							LPFP Vol_Array, double Expiry_Date, LPFP Ext_Force_Disc_Array, 
							short model, long seed, double Window_start, double Window_end,
							long NoSamplings, long No_of_simulations, 
							LPFP DivArray, LPFP DivTimeArray, long DivType, 
							long IntConvention, double Todays_Date, double Days_to_spot,
							double Days_to_Value_Date, double FractExpiryLastTradingDay, 
							short Return_Req, LPFP array_of_dates_to_display);

  /* Return_Req =	1 if Underlying Instrument Price required (dividend dates hidden) ** default,
					2 if Underlying Instrument Price required (dividend dates displayed), 
					3 if Volatility required (dividend dates hidden), 
					4 if Volatility required (dividend dates displayed)  */

double WINAPI UDA_IMPLY_VOL_SURFACE(long Vol_surface_slot_no, LPFP European_Options, 
					  LPFP American_Options, LPFP All_options, LPFP Vol_surface, 
					  LPFP R_Array, double Underlying_Inst_Px, double Ext_Force_Discount, 
					  long model, double Steps_price, LPFP DivArray, long IntConvention,
                      double Todays_Date, double Days_to_spot, double Days_to_Value_Date,
					  double VolTol, long mode);

double WINAPI UDA_IMPLY_VOL_SURFACE2(long Vol_surface_slot_no, LPFP European_Options, 
					  LPFP American_Options, LPFP All_options, LPFP Vol_surface, 
					  LPFP R_Array, double Underlying_Inst_Px, LPFP Ext_Force_Disc_Array, 
					  long model, double Steps_price, LPFP DivArray, long IntConvention,
                      double Todays_Date, double Days_to_spot, double Days_to_Value_Date,
					  double VolTol, long mode);


LPFP WINAPI UDA_DISPLAY_VOL_SURFACE(long Vol_surface_slot_no, double expiryfrom, double expiryto,
                             long expirypoints, double strikefrom,
							 double striketo, long strikepoints, long mode);

LPFP WINAPI UDA_DISPLAY_IMPLICIT_UNDERLYING(
            long Vol_surface_slot_no, double expiryfrom, double expiryto, long expirypoints);

double WINAPI UDA_FREE_VOL_SURFACE(long Vol_surface_slot_no);

LPFP WINAPI UDA_CLOSE_TO_RETURN(LPFP closes);

LPFP WINAPI UDA_ESTIMATOR(LPFP closes, long model, double Interest_rate, double Cyear,
                          double v0, double z0, LPFP xguess);

LPFP WINAPI UDA_ESTIMATOR2(LPFP closes, long model, double Interest_rate, double Cyear,
                          double h0, double z0, LPFP xguess, short mode, short miniterat);
	/* UDA_ESTIMATOR2 is a more advanced version of UDA_ESTIMATOR since it allows
		mode and miniterat to be entered */

char* WINAPI UDA_GARCH_PARAMETER(short model, short param_no);

LPFP WINAPI UDA_RES_VOL(LPFP closes, double Interest_rate,
                        LPFP Garch_Est, double InitialVol, double InitialResidual, short ReturnReq);

double WINAPI UDA_VOL(LPFP Garch_Est, double TimeFrom, double TimeTo);

LPFP WINAPI UDA_NORMALITY_TEST(LPFP Random_Variables);

LPFP WINAPI UDA_NORMAL_RANDOM(long No_of_variables, long seed);

char* WINAPI UDA_STATUS_NAME(double any_double_in_output);

char* WINAPI UDA_PARAM_NAME(double Exotic_Type, short Param_Number);

long	WINAPI UDA_GETMACHINEID( void );
	/* Gets computer id */

long	WINAPI UDA_CHECK_INTEGRITY( long Parameter );

double	WINAPI UDA_SETLICENCE( double LicenceNo );
	/* Enters licence number. Returns no of days to go */

double	WINAPI UDA_GETDAYSTOEXPIRY( void );
		/* Returns no of days before licence expires */

long	WINAPI UDA_DEINSTALL( void );
	/* de-installs licence  */

int		WINAPI UDA_MULTITHREAD(int FLAG);

int		WINAPI UDA_FREE(LPFP pointer);

long	WINAPI UDA_GETVERSION( void );
	   	/* Gets version number for add-in */

/* Advanced UNIVVAR functions -> only for users of UNIVDRV and UNIVVAR */

LPFP WINAPI UVA2_PADZEROS(LPFP InputArray, int no_of_rows);

double WINAPI UVA2_ESTIMATOR_LOG(char *logname);

char* WINAPI UVA2_INPUT_ASSET_DISK(char *fname, char *date_header, char *closes_header,
                                  char *separator, char *from_date, char *to_date,
                                  char *datetype);

char* WINAPI UVA2_INPUT_ASSET_ARRAY(char *name, double from_date,
                                    double to_date, LPFP dateclose);

char* WINAPI UVA2_CREATE_ASSET(char *AssetName, double lastclose);

char* WINAPI UVA2_ASSET_STATUS(char *AssetName);

LPFP WINAPI UVA2_DISPLAY_ACTUAL_CLOSES(char *Asset_or_PortfolioName);

LPFP WINAPI UVA2_DISPLAY_GARCH_ESTIMATION(char *AssetName);

char* WINAPI UVA2_FREE_ASSET(char *AssetName);

LPFP WINAPI UVA2_ESTIMATOR(char *assetname, int model, double r,
                          double Cyear, double h0, double z0, LPFP xguess,
                          short mode, short miniterat);

LPFP WINAPI UVA2_RES_VOL(char *assetname, double r,
                        LPFP x, double v0, double z0, short m);

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

char* WINAPI UVA2_MAKE_PORTFOLIO(char *PortfolioName, pOPER array_of_asset_and_positions, char *date_header,
                                char *closes_header, char *separator, char *fromdate,
                                char *todate, char *datetype, int mode);

char* WINAPI UVA2_GARCH_ANALYSIS(char *PortfolioName, double risk_free_rate, short miniterat,
                                short recalcmode);

char* WINAPI UVA2_FREE_PORTFOLIO(char *PortfolioName);

LPFP WINAPI UVA2_ASSET_SIMULA(char *AssetName, int horizon_days, int no_of_sims,
                             int seed, short mode);

char* WINAPI UVA2_CALC_VCMATRIX(char *PortfolioName, double decay);

LPFP WINAPI UVA2_DISPLAY_VCMATRIX(char *PortfolioName, short mode, double confidence,
                             double timescale, pOPER assetlist);

char* WINAPI UVA2_SET_VCMATRIX(char *PortfolioName, pOPER assetlist,
                              LPFP correlations, LPFP stdeviations,
                              double confidence, double timescale);

char* WINAPI UVA2_UPDATE_CORRELATION(char *PortfolioName, char *firstasset,
                                    char *secondasset, double correlation);

char* WINAPI UVA2_UPDATE_VOLATILITY(char *PortfolioName, char *assetname,
                                   double volatility, double confidence,
                                   double timescale);

LPFP WINAPI UVA2_PORTFOLIO_SIMULA(char *PortfolioName, int horizon_days, int seed,
                                 short technique, pOPER assetlist);

char* WINAPI UVA2_CALC_SCENARIOS(char *PortfolioName, int horizon_days,
                               short technique, double accuracy);

char* WINAPI UVA2_SET_SCENARIOS(char *PortfolioName, LPFP scenarios, int horizon_days);

char* WINAPI UVA2_NON_LINEAR_ANALYSIS(char *PortfolioName, pOPER array_of_positions_and_greeks,
                double horizon_days, short technique, double accuracy);

double WINAPI UVA2_DISPLAY_VAR(char *PortfolioName, double horizon_days,
                            double confidence, short technique);

LPFP WINAPI UVA2_PORTFOLIO_DISTRIBUTION(char *PortfolioName);

pOPER WINAPI UVA2_ASSET_NAMES(char *PortfolioName);

int WINAPI UVA2_FREE_OPER(pOPER oper);

#ifdef __cplusplus
}
#endif  /* __cplusplus */
