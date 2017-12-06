package org.cyberiantiger.slud.net;

public interface TelnetOption {

    /**
     * Top Level Telnet Stuff
     */
    byte SE = (byte) 240;
    byte NOP = (byte) 241;
    byte DM = (byte) 242;
    byte BRK = (byte) 243;
    byte IP = (byte) 244;
    byte AO = (byte) 245;
    byte AYT = (byte) 246;
    byte EC = (byte) 247;
    byte EL = (byte) 248;
    byte GA = (byte) 249;
    byte SB = (byte) 250;
    byte WILL = (byte) 251;
    byte WONT = (byte) 252;
    byte DO = (byte) 253;
    byte DONT = (byte) 254;
    byte IAC = (byte) 255;
    /**
     * Telnet Option Types
     */
    // TOPT_BIN   Binary Transmission                 0  Std   Rec     856  27
    byte TOPT_BIN = 0;
    // TOPT_ECHO  Echo                                1  Std   Rec     857  28
    byte TOPT_ECHO = 1;
    // TOPT_RECN  Reconnection                        2  Prop  Ele     ...
    byte TOPT_RECN = 2;
    // TOPT_SUPP  Suppress Go Ahead                   3  Std   Rec     858  29
    byte TOPT_SUPP = 3;
    // TOPT_APRX  Approx Message Size Negotiation     4  Prop  Ele     ...
    byte TOPT_APRX = 4;
    // TOPT_STAT  Status                              5  Std   Rec     859  30
    byte TOPT_STAT = 5;
    // TOPT_TIM   Timing Mark                         6  Std   Rec     860  31
    byte TOPT_TIM = 6;
    // TOPT_REM   Remote Controlled Trans and Echo    7  Prop  Ele     726
    byte TOPT_REM = 7;
    // TOPT_OLW   Output Line Width                   8  Prop  Ele     ...
    byte TOPT_OLW = 8;
    // TOPT_OPS   Output Page Size                    9  Prop  Ele     ...
    byte TOPT_OPS = 9;
    // TOPT_OCRD  Output Carriage-Return Disposition 10  Hist  Ele     652    *
    byte TOPT_OCRD = 10;
    // TOPT_OHT   Output Horizontal Tabstops         11  Hist  Ele     653    *
    byte TOPT_OHT = 11;
    // TOPT_OHTD  Output Horizontal Tab Disposition  12  Hist  Ele     654    *
    byte TOPT_OHTD = 12;
    // TOPT_OFD   Output Formfeed Disposition        13  Hist  Ele     655    *
    byte TOPT_OFD = 13;
    // TOPT_OVT   Output Vertical Tabstops           14  Hist  Ele     656    *
    byte TOPT_OVT = 14;
    // TOPT_OVTD  Output Vertical Tab Disposition    15  Hist  Ele     657    *
    byte TOPT_OVTD = 15;
    // TOPT_OLD   Output Linefeed Disposition        16  Hist  Ele     658    *
    byte TOPT_OLD = 16;
    // TOPT_EXT   Extended ASCII                     17  Prop  Ele     698
    byte TOPT_EXT = 17;
    // TOPT_LOGO  Logout                             18  Prop  Ele     727
    byte TOPT_LOGO = 18;
    // TOPT_BYTE  Byte Macro                         19  Prop  Ele     735
    byte TOPT_BYTE = 19;
    // TOPT_DATA  Data Entry Terminal                20  Prop  Ele    1043
    byte TOPT_DATA = 20;
    // TOPT_SUP   SUPDUP                             21  Prop  Ele     736
    byte TOPT_SUP = 21;
    // TOPT_SUPO  SUPDUP Output                      22  Prop  Ele     749
    byte TOPT_SUPO = 22;
    // TOPT_SNDL  Send Location                      23  Prop  Ele     779
    byte TOPT_SNDL = 23;
    // TOPT_TERM  Terminal Type                      24  Prop  Ele    1091
    byte TOPT_TERM = 24;
    // TOPT_EOR   End of Record                      25  Prop  Ele     885
    byte TOPT_EOR = 25;
    // TOPT_TACACS  TACACS User Identification       26  Prop  Ele     927
    byte TOPT_TACACS = 26;
    // TOPT_OM    Output Marking                     27  Prop  Ele     933
    byte TOPT_OM = 27;
    // TOPT_TLN   Terminal Location Number           28  Prop  Ele     946
    byte TOPT_TLN = 28;
    // TOPT_3270  Telnet 3270 Regime                 29  Prop  Ele    1041
    byte TOPT_3270 = 29;
    // TOPT_X.3   X.3 PAD                            30  Prop  Ele    1053
    byte TOPT_X_3 = 30;
    // TOPT_NAWS  Negotiate About Window Size        31  Prop  Ele    1073
    byte TOPT_NAWS = 31;
    // TOPT_TS    Terminal Speed                     32  Prop  Ele    1079
    byte TOPT_TS = 32;
    // TOPT_RFC   Remote Flow Control                33  Prop  Ele    1372
    byte TOPT_RFC = 33;
    // TOPT_LINE  Linemode                           34  Draft Ele    1184
    byte TOPT_LINE = 34;
    // TOPT_XDL   X Display Location                 35  Prop  Ele    1096
    byte TOPT_XDL = 35;
    // TOPT_ENVIR Telnet Environment Option          36  Hist  Not    1408
    //byte TOPT_ENVIR = 36;
    // TOPT_AUTH  Telnet Authentication Option       37  Exp   Ele    1416
    byte TOPT_AUTH = 37;
    // TOPT_ENVIR Telnet Environment Option          39  Prop  Ele    1572
    byte TOPT_ENVIR = 39;
    // TOPT_TN3270E TN3270 Enhancements              40  Draft Ele    2355    *
    byte TOPT_TN3270E = 40;
    // TOPT_XAUTH  Telnet XAUTH                      41  Exp
    byte TOPT_XAUTH = 41;
    // TOPT_CHARSET Telnet CHARSET                   42  Exp          2066
    byte TOPT_CHARSET = 42;
    // TOPT_RSP   Telnet Remote Serial Port          43  Exp
    byte TOPT_RSP = 43;
    // TOPT_COMPORT Telnet Com Port Control          44  Exp          2217
    byte TOPT_COMPORT = 44;
    // TOPT_SLE   Telnet Suppress Local Echo         45  Exp                  *
    byte TOPT_SLE = 45;
    // TOPT_STARTTLS Telnet Start TLS                46  Exp                  *
    byte TOPT_STARTTLS = 46;
    // TOPT_KERMIT   Telnet KERMIT                   47  Exp                  *
    byte TOPT_KERMIT = 47;
    // TOPT_SEND-URL Send-URL                        48  Exp                  *
    byte TOPT_SEND_URL = 48;
    // TOPT_EXTOP Extended-Options-List             255  Std   Rec     861  32
    byte TOPT_EXTOP = (byte) 255;
    // GMCP
    byte TOPT_GMCP = (byte) 201;

    // First Byte in Subnegotation: IAC SB <OPTION> <SEND/IS> <DATA> IAC SE
    byte IS = 0;
    byte SEND = 1;

}
