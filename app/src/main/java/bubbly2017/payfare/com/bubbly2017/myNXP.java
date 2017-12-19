package bubbly2017.payfare.com.bubbly2017;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.tech.MifareClassic;

import com.nxp.nfclib.classic.MFClassic;
import com.nxp.nfclib.exceptions.ClassicException;
import com.nxp.nfclib.exceptions.PlusException;
import com.nxp.nfclib.exceptions.SAMException;
import com.nxp.nfclib.exceptions.SmartCardException;
import com.nxp.nfclib.icode.ICodeSLI;
import com.nxp.nfclib.icode.ICodeSLIL;
import com.nxp.nfclib.icode.ICodeSLIS;
import com.nxp.nfclib.icode.ICodeSLIX;
import com.nxp.nfclib.icode.ICodeSLIX2;
import com.nxp.nfclib.icode.ICodeSLIXL;
import com.nxp.nfclib.icode.ICodeSLIXS;
import com.nxp.nfclib.ntag.NTag;
import com.nxp.nfclib.ntag.NTag203x;
import com.nxp.nfclib.ntag.NTag210;
import com.nxp.nfclib.ntag.NTag213215216;
import com.nxp.nfclib.ntag.NTag213F216F;
import com.nxp.nfclib.ntag.NTagI2C;
import com.nxp.nfclib.plus.PlusSL1;
import com.nxp.nfclib.ultralight.Ultralight;
import com.nxp.nfclib.ultralight.UltralightC;
import com.nxp.nfclib.ultralight.UltralightEV1;
import com.nxp.nfclib.utils.NxpLogUtils;
import com.nxp.nfclib.utils.Utilities;
import com.nxp.nfcliblite.Interface.NxpNfcLibLite;
import com.nxp.nfcliblite.Interface.Nxpnfcliblitecallback;
import com.nxp.nfcliblite.cards.DESFire;
import com.nxp.nfcliblite.cards.Plus;
public class myNXP {



    /** Create lib lite instance. */
    private NxpNfcLibLite libInstance = null;
    /** Mifare DESFire instance initiated. */
    private DESFire mDESFire;
    //private NfcA mNfcA;
    /** Mifare MFClassic instance initiated. */
    private MFClassic classic;
    private MifareClassic classsic2;
    /** Mifare Ultralight instance initiated. */
    private Ultralight mifareUL;
    /** Mifare Ultralight instance initiated. */
    private UltralightC objUlCardC;
    /** Mifare Ultralight EV1 instance initiated. */
    private UltralightEV1 objUlCardEV1;
    /** Mifare Plus instance initiated. */
    private Plus plus;

    public static String strResult;

    /** Mifare Plus SL1 instance initiated. */
    private PlusSL1 plusSL1;

    /** ICode SLI instance initiated. */
    private ICodeSLI iCodeSli;
    /** ICode SLI-L instance initiated. */
    private ICodeSLIL iCodeSliL;
    /** ICode SLI-S instance initiated. */
    private ICodeSLIS iCodeSliS;
    /** ICode SLI-X instance initiated. */
    private ICodeSLIX iCodeSliX;
    /** ICode SLI-XL instance initiated. */
    private ICodeSLIXL iCodeSliXL;
    /** ICode SLI-XS instance initiated. */
    private ICodeSLIXS iCodeSliXS;
    /** ICode SLIX2 instance initiated. */
    private ICodeSLIX2 iCodeSliX2;

    /** Create imageView instance. */
    private ImageView mImageView = null;
    // private static Handler mHandler;
    /** Create Textview instance initiated. */
    private TextView tv = null;
    /**
     * Ultralight First User Memory Page Number.
     */
    private static final int DEFAULT_PAGENO_ULTRALIGHT = 4;
    /**
     * Variable DATA Contain a String.
     */
    private static final String DATA = "This is the data";

    /**
     * KEY_APP_MASTER key used for encrypt data.
     */
    private static final String KEY_APP_MASTER = "This is my key  ";
    /** */
    private byte[] bytesKey = null;
    /** */
    private Cipher cipher = null;
    /** */
    private IvParameterSpec iv = null;
    public static int iCardBalance=0;
    public boolean isGoodSale=false;
    public static String mCurCardUID;
    public static String mCurrentKeyB;
    static final String TAG = "myMXP";
    public static boolean AuthresGood = false;



    public myNXP(NxpNfcLibLite thelibInstance,MFClassic theclassic){

        libInstance =  thelibInstance ;
        classic = theclassic;
        //libInstance = NxpNfcLibLite.getInstance();
    }

    /**
     * Initialize the Cipher and init vector of 16 bytes with 0xCD.
     */

    private void initializeCipherinitVector() {

		/* Initialize the Cipher */
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        } catch (NoSuchPaddingException e) {

            e.printStackTrace();
        }

		/* set Application Master Key */
        bytesKey = KEY_APP_MASTER.getBytes();

		/* Initialize init vector of 16 bytes with 0xCD. It could be anything */
        byte[] ivSpec = new byte[16];
        Arrays.fill(ivSpec, (byte) 0xCD);
        iv = new IvParameterSpec(ivSpec);

    }






    private void showCardDetails(Object cardDetailsObj){

    }

    protected void onNewIntent(final Intent intent) {

        //Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //long[] duration = { 50, 100, 200, 300 };
        //vib.vibrate(duration, -1);

/*
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.e(TAG,"TagFromIntent:"+ tagFromIntent.toString());
        String curTechlist[] = tagFromIntent.getTechList();
        boolean isClassicSupported=false;
        for(int i=0;i<curTechlist.length;i++){

            if(curTechlist[i].contains("Classic")){
                isClassicSupported=true;
                 break;
            }
        }
        if(isClassicSupported) {
            classsic2 = MifareClassic.get(tagFromIntent);
            Log.e(TAG, Integer.toString(classsic2.getType()));
            int MFSize = classsic2.getSize();
        }
        else
        {
            Log.e(TAG,"NO Classic card //\n Classic card not supported by this device");

        }
		// MifareUltralight.get(tag)
		*/

        libInstance.filterIntent(intent, new Nxpnfcliblitecallback() {

            @Override
            public void onUltraLightCardDetected(final Ultralight objUlCard) {
                mifareUL = objUlCard;
				/* Insert your logic here by commenting the function call below. */
                try {
                    mifareUL.connect();


                    ultralightCardLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onUltraLightCCardDetected(final UltralightC ulC) {
                objUlCardC = ulC;
				/*
				 * Insert your logic here by commenting the function call below
				 */
                try {
                    objUlCardC.connect();
                    ultralightcCardLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }

            }

            @Override
            public void onUltraLightEV1CardDetected(final UltralightEV1 ulEV1) {
                objUlCardEV1 = ulEV1;
				/*
				 * Insert your logic here by commenting the function call below
				 */
                try {
                    objUlCardEV1.connect();
                    ultralightEV1CardLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onClassicCardDetected(final MFClassic objMFCCard) {
                classic = objMFCCard;
				/* Insert your logic here by commenting the function call below. */
                try {
                    classic.connect();
                    classicCardLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onDESFireCardDetected(final DESFire objDESFire) {
                mDESFire = objDESFire;
				/* Insert your logic here by commenting the function call below. */
                try {
                    mDESFire.connect();
                    desfireCardLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }

            }


            @Override
            public void onPlusCardDetected(final Plus objMFPlus) {
                plus = objMFPlus;
                try {
                    //plus.connect();
                    //plusCardLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onPlusSL1CardDetected(PlusSL1 objPlusSL1) {
                plusSL1 = objPlusSL1;
                classic = objPlusSL1; // Plus SL1 is completely compatible with
                // Classic!!
                try {
                    plusSL1.connect();
                    PlusSL1CardLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onICodeSLIDetected(final ICodeSLI objiCodesli) {
                iCodeSli = objiCodesli;

                try {
                    iCodeSli.connect();
                    iCodeSLILogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onICodeSLILDetected(final ICodeSLIL objiCodeslil) {
                iCodeSliL = objiCodeslil;

                try {
                    iCodeSliL.connect();
                    iCodeSLIlLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onICodeSLISDetected(final ICodeSLIS objiCodeslis) {
                iCodeSliS = objiCodeslis;

                try {
                    iCodeSliS.connect();
                    iCodeSLIsLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onICodeSLIXDetected(final ICodeSLIX objiCodeslix) {
                iCodeSliX = objiCodeslix;

                try {
                    iCodeSliX.connect();
                    iCodeSLIxLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onICodeSLIXLDetected(final ICodeSLIXL objiCodeslixl) {
                iCodeSliXL = objiCodeslixl;

                try {
                    iCodeSliXL.connect();
                    iCodeSLIxlLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onICodeSLIXSDetected(final ICodeSLIXS objiCodeslixs) {
                iCodeSliXS = objiCodeslixs;

                try {
                    iCodeSliXS.connect();
                    iCodeSLIxsLogic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }
            }

            @Override
            public void onICodeSLIX2Detected(final ICodeSLIX2 objiCodeslix2) {
                iCodeSliX2 = objiCodeslix2;

                try {
                    iCodeSliX2.connect();
                    iCodeSLIx2Logic();
                } catch (Throwable t) {
                    showMessage("Unknown Error Tap Again!", 't');
                }

            }

            @Override
            public void onNTag203xCardDetected(final NTag203x objnTag203x) {
                try {
                    objnTag203x.connect();
                    ntagCardLogic(objnTag203x);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onNTag210CardDetected(final NTag210 objnTag210) {
                try {
                    objnTag210.connect();
                    ntagCardLogic(objnTag210);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onNTag213215216CardDetected(
                    final NTag213215216 objnTag213215216) {
                try {
                    objnTag213215216.connect();
                    ntagCardLogic(objnTag213215216);
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onNTag213F216FCardDetected(
                    final NTag213F216F objnTag213216f) {
                try {
                    objnTag213216f.connect();
                    ntagCardLogic(objnTag213216f);
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onNTagI2CCardDetected(final NTagI2C objnTagI2c) {
                try {
                    objnTagI2c.connect();
                    ntagCardLogic(objnTagI2c);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * iCode SLIXS Card Logic.
     */
    protected void iCodeSLIxsLogic() {


        //showImageSnap(R.drawable.icode_p);
        tv.setText(" ");
		/* Get the Icode label name. */
        showMessage("Card Detected : " + iCodeSliXS.getTagName(), 'n');

        try {
			/* Get the UID */
            byte[] uid = iCodeSliXS.getUID();
			/* Display message in text view */
            showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* It should contain four bytes of data to be write. */
            byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03,
                    (byte) 0x04 };
			/* Display message in text view */
            showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
			/* Write the data in specified block. */
            iCodeSliXS.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5,
                    writeData);
			/* Read the data in specified block. */
            byte[] read = iCodeSliXS.readSingleBlock(
                    ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
            showMessage("Read: " + Utilities.dumpBytes(read), 'd');
            showCardDetails(iCodeSliXS.getCardDetails());
			/* Close the connection. */
            iCodeSliXS.close();

        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }

    }

    protected void iCodeSLIxlLogic() {


        // showImageSnap(R.drawable.icode_p);
        tv.setText(" ");
		/* Get the Icode label name. */
        showMessage("Card Detected : " + iCodeSliXL.getTagName(), 'n');

        try {
			/* Get the UID */
            byte[] uid = iCodeSliXL.getUID();
			/* Display message in text view */
            showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* It should contain four bytes of data to be write. */
            byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03,
                    (byte) 0x04 };
			/* Display message in text view */
            showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
			/* Write the data in specified block. */
            iCodeSliXL.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5,
                    writeData);
			/* Read the data in specified block. */
            byte[] read = iCodeSliXL.readSingleBlock(
                    ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
            showMessage("Read: " + Utilities.dumpBytes(read), 'd');
            showCardDetails(iCodeSliXL.getCardDetails());
			/* Close the connection. */
            iCodeSliXL.close();

        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }

    }

    /**
     * iCode SLIX Card Logic.
     */
    protected void iCodeSLIxLogic() {


        //showImageSnap(R.drawable.icode_p);
        tv.setText(" ");
		/* Get the Icode label name. */
        showMessage("Card Detected : " + iCodeSliX.getTagName(), 'n');

        try {
			/* Get the UID */
            byte[] uid = iCodeSliX.getUID();
			/* Display message in text view */
            showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* It should contain four bytes of data to be write. */
            byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03,
                    (byte) 0x04 };
			/* Display message in text view */
            showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
			/* Write the data in specified block. */
            iCodeSliX.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5,
                    writeData);
			/* Read the data in specified block. */
            byte[] read = iCodeSliX.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS,
                    (byte) 5);
			/* Display message in text view. */
            showMessage("Read: " + Utilities.dumpBytes(read), 'd');
            showCardDetails(iCodeSliX.getCardDetails());
			/* Close the connection. */
            iCodeSliX.close();

        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }
    }

    /**
     * iCode slis Card Logic.
     */
    protected void iCodeSLIsLogic() {


        // showImageSnap(R.drawable.icode_p);
        tv.setText(" ");
		/* Get the Icode label name. */
        showMessage("Card Detected : " + iCodeSliS.getTagName(), 'n');

        try {
			/* Get the UID */
            byte[] uid = iCodeSliS.getUID();
			/* Display message in text view */
            showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* It should contain four bytes of data to be write. */
            byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03,
                    (byte) 0x04 };
			/* Display message in text view */
            showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
			/* Write the data in specified block. */
            iCodeSliS.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5,
                    writeData);
			/* Read the data in specified block. */
            byte[] read = iCodeSliS.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS,
                    (byte) 5);
			/* Display message in text view. */
            showMessage("Read: " + Utilities.dumpBytes(read), 'd');
            showCardDetails(iCodeSliS.getCardDetails());
			/* Close the connection. */
            iCodeSliS.close();

        } catch (TagLostException e) {
            showMessage("TagLost Exception - Tap Again!", 't');
            e.printStackTrace();
        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }

    }

    /**
     * icode sli card logic.
     */
    protected void iCodeSLILogic() {

        // showImageSnap(R.drawable.icode_p);
        tv.setText(" ");
		/* Get the Icode label name. */
        showMessage("Card Detected : " + iCodeSli.getTagName(), 'n');

        try {
			/* Get the UID */
            byte[] uid = iCodeSli.getUID();
			/* Display message in text view */
            showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* It should contain four bytes of data to be write. */
            byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03,
                    (byte) 0x04 };
			/* Display message in text view */
            showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
			/* Write the data in specified block. */
            iCodeSli.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5,
                    writeData);
			/* Read the data in specified block. */
            byte[] read = iCodeSli.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS,
                    (byte) 5);
			/* Display message in text view. */
            showMessage("Read: " + Utilities.dumpBytes(read), 'd');
            showCardDetails(iCodeSli.getCardDetails());
			/* Close the connection. */
            iCodeSli.close();

        } catch (TagLostException e) {
            showMessage("TagLost Exception - Tap Again!", 't');
            e.printStackTrace();
        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }
    }

    /**
     * icode sli card logic.
     */
    protected void iCodeSLIlLogic() {

        //showImageSnap(R.drawable.icode_p);
        tv.setText(" ");
		/* Get the Icode label name. */
        showMessage("Card Detected : " + iCodeSliL.getTagName(), 'n');

        try {
			/* Get the UID */
            byte[] uid = iCodeSliL.getUID();
			/* Display message in text view */
            showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* It should contain four bytes of data to be write. */
            byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03,
                    (byte) 0x04 };
			/* Display message in text view */
            showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
			/* Write the data in specified block. */
            iCodeSliL.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5,
                    writeData);
			/* Read the data in specified block. */
            byte[] read = iCodeSliL.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS,
                    (byte) 5);
			/* Display message in text view. */
            showMessage("Read: " + Utilities.dumpBytes(read), 'd');
            showCardDetails(iCodeSliL.getCardDetails());
			/* Close the connection. */
            iCodeSliL.close();

        } catch (TagLostException e) {
            showMessage("TagLost Exception - Tap Again!", 't');
            e.printStackTrace();
        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }

    }

    /**
     * icode slix2 card logic.
     */
    protected void iCodeSLIx2Logic() {

        //showImageSnap(R.drawable.icode_p);
        tv.setText(" ");
		/* Get the Icode label name. */
        showMessage("Card Detected : " + iCodeSliX2.getTagName(), 'n');

        try {
			/* Get the UID */
            byte[] uid = iCodeSliX2.getUID();
			/* Display message in text view */
            showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* It should contain four bytes of data to be write. */
            byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03,
                    (byte) 0x04 };
			/* Display message in text view */
            showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
			/* Write the data in specified block. */
            iCodeSliX2.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5,
                    writeData);
			/* Read the data in specified block. */
            byte[] read = iCodeSliX2.readSingleBlock(
                    ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
            showMessage("Read: " + Utilities.dumpBytes(read), 'd');
			/* Close the connection. */
            iCodeSliX2.close();

        } catch (TagLostException e) {
            showMessage("TagLost Exception - Tap Again!", 't');
            e.printStackTrace();
        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }

    }

    /**
     * Mifare DESFire Card Logic.
     *
     * @throws SmartCardException
     */
    protected void desfireCardLogic() throws SmartCardException {

        // showImageSnap(R.drawable.desfire_ev1);
        tv.setText(" ");
        showMessage("Card Detected : " + mDESFire.getCardDetails().cardName,
                'n');

        try {
            mDESFire.setTimeout(2000);
            testDESFireFormat();
            testDESFirepersonalize();
            testDESFireauthenticate();
            testDESFireupdatePICCMasterKey();
            testDESFireauthenticate();
            testDESFireupdateApplicationMasterKey();
            testDESFireauthenticate();
            testDESFireWrite();
            testDESFireRead();
            mDESFire.setTimeout(2000);
            showCardDetails(mDESFire.getCardDetails());
            testDESFireFormat();
            mDESFire.close();
        } catch (IOException cause) {
            cause.printStackTrace();
        }
    }



    /**
     * Ntag Operations are, getTagname(), getUID(), Write and Read.
     *
     * @param tag
     *            object
     */
    private void ntagCardLogic(final NTag tag) {

        //showImageSnap(R.drawable.ntag_p);
        tv.setText(" ");
        showMessage("Card Detected : " + tag.getTagName(), 'd');

        try {
            NxpLogUtils.d(TAG, "testBasicNtagFunctionality, start");
            tag.connect();
            showMessage("UID of the Tag: " + Utilities.dumpBytes(tag.getUID()),
                    'd');
            showMessage("Tag Name: " + tag.getType().name(), 'd');
            for (int idx = tag.getFirstUserpage(); (idx < tag.getLastUserPage())
                    && idx <= 5; idx++) {
                byte[] data = new byte[] { (byte) idx, (byte) idx, (byte) idx,
                        (byte) idx };
                tag.write(idx, data);
                showMessage("Written 4 Bytes Data at page No= " + idx + " "
                        + Utilities.dumpBytes(data), 'd');
            }
            for (int idx = tag.getFirstUserpage(); (idx < tag.getLastUserPage())
                    && idx <= 5; idx++) {
                showMessage("Read 16 Bytes of Data from page No= " + idx + " "
                        + Utilities.dumpBytes(tag.read(idx)), 'd');
            }
            showCardDetails(tag.getCardDetails());
            tag.close();
            NxpLogUtils.d(TAG, "testBasicNtagFunctionality, End");
        } catch (TagLostException e) {
            showMessage("TagLost Exception - Tap Again!", 't');
            e.printStackTrace();
        } catch (IOException e) {
            showMessage("IO Exception -  Check logcat!", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {
            showMessage("SmartCard Exception - Check logcat!", 't');
            e.printStackTrace();
        } catch (Throwable t) {
            showMessage("Exception - Check logcat!", 't');
            t.printStackTrace();
        }
    }

    /** DESFire read IO Operations. */
    private void testDESFireRead() {

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testDESFireRead, start");
            byte[] data = mDESFire.read(5);
            res = true;
            showMessage(
                    "Data Read from the card..." + Utilities.dumpBytes(data),
                    'd');
        } catch (SmartCardException e) {
            showMessage("Data Read from the card: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testDESFireRead, result is " + res);
        NxpLogUtils.d(TAG, "testDESFireRead, End");
    }

    /** DESFire Write IO Operations. */
    private void testDESFireWrite() {

        byte[] data = new byte[] { 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
                0x11 };

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testDESFireWrite, start");
            mDESFire.write(data);
            res = true;
            showMessage("Data Written: " + Utilities.dumpBytes(data), 'd');
        } catch (SmartCardException e) {
            showMessage("Data Written: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testDESFireWrite, result is " + res);
        NxpLogUtils.d(TAG, "testDESFireWrite, End");

    }

    /** DESFire Update Application master key IO Operations. */
    private void testDESFireupdateApplicationMasterKey() {
        byte[] oldKey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        byte[] newKey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

        byte[] masterKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

        byte[] appId = { 0x12, 0x12, 0x12 };
        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testDESFireupdateApplicationMasterKey, start");
            mDESFire.updateApplicationMasterKey(masterKey, appId, oldKey,
                    newKey);
            res = true;
            showMessage("Update Application MasterKey: " + res, 'd');
        } catch (SmartCardException e) {
            showMessage("Update Application MasterKey: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testDESFireupdateApplicationMasterKey, result is "
                + res);
        NxpLogUtils.d(TAG, "testDESFireupdateApplicationMasterKey, End");
    }

    /** DESFire Authenticate IO Operations . */
    private void testDESFireauthenticate() {
        byte[] masterKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        byte[] appId = { 0x12, 0x12, 0x12 };
        byte[] appkey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testDESFireauthenticate, start");
            mDESFire.authenticate(masterKey, appId, appkey);
            res = true;
            showMessage("Authenticate: " + res, 'd');
        } catch (SmartCardException e) {
            showMessage("Authenticate: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testDESFireauthenticate, result is " + res);
        NxpLogUtils.d(TAG, "testDESFireauthenticate, End");
    }

    /** DESFire personalize Operations. */
    private void testDESFirepersonalize() {
        byte[] mykey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        byte[] appKey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testDESFirepersonalize, start");

            mDESFire.personalize(mykey, new byte[] { 0x12, 0x12, 0x12 }, appKey);
            res = true;
            showMessage("personalize: " + res, 'd');
        } catch (SmartCardException e) {
            showMessage("personalize: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testDESFirepersonalize, result is " + res);
        NxpLogUtils.d(TAG, "testDESFirepersonalize, End");

    }

    /** DESFire update PICC Master key Operations . */
    private void testDESFireupdatePICCMasterKey() {
        byte[] oldKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        byte[] newKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testDESFireupdatePICCMasterKey, start");
            mDESFire.updatePICCMasterKey(oldKey, newKey);
            res = true;
            showMessage("DESFire Update PICC Master Key: " + res, 'd');
        } catch (SmartCardException e) {
            showMessage("DESFire Update PICC Master Key: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testDESFireupdatePICCMasterKey, result is " + res);
        NxpLogUtils.d(TAG, "testDESFireupdatePICCMasterKey, End");

    }

    /** DESFire Format Operations . */
    private void testDESFireFormat() {
        byte[] mykey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testDESFireFormat, start");
            mDESFire.format(mykey);
            res = true;
            showMessage("Format: " + res, 'd');
        } catch (SmartCardException e) {
            showMessage("Format: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testDESFireFormat, result is " + res);
        NxpLogUtils.d(TAG, "testDESFireFormat, End");
    }


    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Mifare classic Card Logic.
     *
     * @throws SmartCardException
     */
    public String classicCardLogic() throws SmartCardException {

        // showImageSnap(R.drawable.classic);
        //tv.setText(" ");
        //showMessage("Card Detected : " + classic.getCardDetails().cardName, 'n');

        String strFinalRetMessage="";

        try {
            // classic.connect();

            // ***use below code only for Plus cards. It show how to switch from
            // Plus Security Level 1 to Security Level 3
            // byte[] switchKey = new byte[] { (byte) 0x00,
            // (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            // (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            // (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
            // };
            // try {
            // classic.switchToSL3(switchKey);//give sl3 switchKey
            // showMessage("Successfully switched to Plus Security Level 3. Tap again to continue "
            // , 'd');
            // } catch (AuthenticationException e) {
            //
            // e.printStackTrace();
            // } catch (GeneralSecurityException e) {
            //
            // e.printStackTrace();
            // } catch (SmartCardException e) {
            //
            // e.printStackTrace();
            // }
            // *** end of Plus variant code

            //showMessage("Uid :" + Utilities.dumpBytes(classic.getUID()), 'd');
            mCurCardUID = Utilities.dumpBytes(classic.getUID()).substring(2,10);
            Log.d(TAG,"UID: " + mCurCardUID);

            mCurrentKeyB = mCurCardUID;

            //String MFkey = "08" + MFSerTmp.Substring(0, 2) + MFSerTmp.Substring(6, 2) + "08" + MFSerTmp.Substring(4, 2) + MFSerTmp.Substring(2, 2);

            //Load Key
            //APDU Sent: FF 82 20 00 06
            // 08 9B 07 08 4F F4
            //APDU RECV: 90 00

            mCurrentKeyB = "08" + mCurCardUID.substring(0, 2) + mCurCardUID.substring(6, 8) + "08" + mCurCardUID.substring(4, 6) + mCurCardUID.substring(2, 4);

            classic.setTimeout(3000);
            //String buffRead = testClassicRead(0);
            //testClassicformat();
            //testClassicpersonalize();
            //testClassicupdateMasterKey();
            byte btSector = 2;
            strResult="";
            byte [] btREs = new byte[] {(byte)0x00, btSector};
            testClassicauthenticate(mCurrentKeyB,(byte)0);

            if(AuthresGood) {
                strResult="";
                strResult = testClassicRead(0);
                strFinalRetMessage += "Sector 0 result:\n" + strResult;
                AuthresGood=false;
                testClassicauthenticate(mCurrentKeyB, (byte)0);
                if(strResult.length()==0)
                    return "return read sec 0 failed";

                if(AuthresGood) {
                    strResult="";
                    strResult = testClassicRead(btSector);

                    if(strResult.length()==0)
                        return "return read sec 2 failed";

                    strFinalRetMessage += "\nSector 2 result:\n" + strResult;
                    classic.close();


                    return strFinalRetMessage;
                }else{

                    classic.close();
                    return   "auth sec 2 fail";
                    //return strFinalRetMessage;
                }

            }else {
                strFinalRetMessage +=    "auth sec 0 fail";
                classic.close();
                return strFinalRetMessage;
            }


            //testClassicWrite();




            // byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte)
            // 0xFF,
            // (byte) 0xFF, (byte) 0xFF };
            //
            // classic.authenticateSectorWithKeyA(0, key);
            // showCardDetails(classic.getCardDetails());

        } catch (IOException e) {

            showMessage("IOException: See Logcat!", 't');
            e.printStackTrace();
        }
        return "auth and read fail";
    }


    public String bubblyCardBalanceRequest() throws SmartCardException {



        String strFinalRetMessage="";

        try {


            mCurCardUID = Utilities.dumpBytes(classic.getUID()).substring(2,10);
            Log.d(TAG,"UID: " + mCurCardUID);

            mCurrentKeyB = mCurCardUID;


            mCurrentKeyB = "08" + mCurCardUID.substring(0, 2) + mCurCardUID.substring(6, 8) + "08" + mCurCardUID.substring(4, 6) + mCurCardUID.substring(2, 4);

            classic.setTimeout(5000);

            byte btSector = 2;
            strResult="";
            byte [] btREs = new byte[] {(byte)0x00, btSector};
            testBubblyauthenticate(mCurrentKeyB,(byte)0);

            if(AuthresGood) {
                strResult="";
                strResult = testClassicRead(0);
                strFinalRetMessage += "Sector 0 result:\n" + strResult;
                AuthresGood=false;
                testBubblyauthenticate(mCurrentKeyB, (byte)0);
                if(strResult.length()==0)
                    return "return read sec 0 failed";

                if(AuthresGood) {
                    strResult="";
                    strResult = testClassicRead(btSector);

                    if(strResult.length()==0)
                        return "return read sec 2 failed";

                    iCardBalance = Integer.parseInt(strResult.substring(2,4),16);


                    strFinalRetMessage += "\nSector 2 result:\n" + strResult;





                    return strFinalRetMessage;
                }else{

                    classic.close();
                    return   "auth sec 2 fail";
                    //return strFinalRetMessage;
                }

            }else {
                strFinalRetMessage +=    "auth sec 0 fail";
                classic.close();
                return strFinalRetMessage;
            }




        } catch (IOException e) {

            showMessage("IOException: See Logcat!", 't');
            e.printStackTrace();
        }
        return "auth and read fail";
    }


    public String bubblyCardDecrement(int iDecementValue) throws SmartCardException {



        String strFinalRetMessage="";
        isGoodSale=false;
        try {

            mCurCardUID = Utilities.dumpBytes(classic.getUID()).substring(2,10);
            Log.d(TAG,"UID: " + mCurCardUID);

            mCurrentKeyB = mCurCardUID;

            //String MFkey = "08" + MFSerTmp.Substring(0, 2) + MFSerTmp.Substring(6, 2) + "08" + MFSerTmp.Substring(4, 2) + MFSerTmp.Substring(2, 2);

            //Load Key
            //APDU Sent: FF 82 20 00 06
            // 08 9B 07 08 4F F4
            //APDU RECV: 90 00

            mCurrentKeyB = "08" + mCurCardUID.substring(0, 2) + mCurCardUID.substring(6, 8) + "08" + mCurCardUID.substring(4, 6) + mCurCardUID.substring(2, 4);

            classic.setTimeout(3000);
            //String buffRead = testClassicRead(0);
            //testClassicformat();
            //testClassicpersonalize();
            //testClassicupdateMasterKey();
            byte btSector = 2;
            strResult="";
            byte [] btREs = new byte[] {(byte)0x00, btSector};
            testBubblyauthenticate(mCurrentKeyB,(byte)0);

            if(AuthresGood) {
                strResult="";
                strResult = testClassicRead(0);
                strFinalRetMessage += "Sector 0 result:\n" + strResult;
                AuthresGood=false;
                testBubblyauthenticate(mCurrentKeyB, (byte)0);
                if(strResult.length()==0)
                    return "return read sec 0 failed";
                testBubblyauthenticate(mCurrentKeyB, (byte)0);
                if(AuthresGood) {

                    //public void decrement(int blockIndex,int value)
                    classic.decrement(2,iDecementValue);
                    classic.transfer(2);

                    testBubblyauthenticate(mCurrentKeyB, (byte)0);

                    strResult="";
                    strResult = testClassicRead(btSector);

                    if(strResult.length()==0)
                        return "return read sec 2 failed";

                    iCardBalance = Integer.parseInt(strResult.substring(2,4),16);



                    strFinalRetMessage += "\nSector 2 result:\n" + strResult;
                    isGoodSale=true;

                    //classic.close();


                    return strFinalRetMessage;
                }else{

                    classic.close();
                    return   "auth sec 2 fail";
                    //return strFinalRetMessage;
                }

            }else {
                strFinalRetMessage +=    "auth sec 0 fail";
                classic.close();
                return strFinalRetMessage;
            }




        } catch (IOException e) {

            showMessage("IOException: See Logcat!", 't');
            e.printStackTrace();
        }
        return "auth and read fail";
    }

    public boolean bubblyCardUpdateCardHistory(String strMerchantDisp,String strTransActionDetails){

        //Update 2 sectors for history:
        // 1'st sector contains merchant name
        // 2'd sector contains rans details:

        /*
        AFTER SALE 2 =  4 tokerns, blok 6 org valu =4
        Sec A=20202020506F6E677261637A20202020
        Sec B=07533717111711300114310000040153
        format: time-date-termsn-transamt-cardbal
            Sector A =  block number read from block 6 + 1 * 4 (eg: if blk 6's value = 3, then sec a = (3+1) * 4=16)
            Sector B = Sectory A result + 1
         */
        int currentStoragePlace = 0;
        int BlockA=0;
        int BlockB=0;
        String strBlock6Value="";
        byte btSector = 2;
        byte btHistoryIndexBlock=6;
        if (strTransActionDetails.trim().length()==0||strMerchantDisp.trim().length()==0)
            return false;

        try {

            if(!classic.isConnected()) {
                return false;
            }


            if (!testBubblyLogAuthenticate( classic.blockToSector(btHistoryIndexBlock))) {
                return false;
            }

            strBlock6Value = testClassicRead(btHistoryIndexBlock).trim();

            currentStoragePlace = Integer.parseInt(strBlock6Value.replace("0x", ""));

            if (currentStoragePlace >= 0) {
                BlockA = (currentStoragePlace + 1) * 4;
                BlockB = BlockA + 1;

            }
            Log.d(TAG,"Block A: "+ BlockA);

            if (!testBubblyLogAuthenticate(classic.blockToSector(BlockA))) {

                return false;
            }
            Log.d(TAG,"Block A Data to write: "+ strMerchantDisp);
            if(!testClassicWriteBlock(BlockA,strMerchantDisp)) { //write sector A and B
                return false;
            }

            Log.d(TAG,"Block B: "+ BlockB);

            if (!testBubblyLogAuthenticate( classic.blockToSector(BlockB))) {

                return false;
            }


            Log.d(TAG,"Block B Data to write: "+ strTransActionDetails);

            if(!testClassicWriteBlock(BlockB,strTransActionDetails)) { //write sector A and B
                return false;
            }

            if (!testBubblyLogAuthenticate( classic.blockToSector(btHistoryIndexBlock))) {
                return false;
            }

            currentStoragePlace++;
            if(currentStoragePlace>6){
                currentStoragePlace=0;
            }

            Log.d(TAG,"Update History Index: "+ String.format("%032d", currentStoragePlace ));
            if(!testClassicWriteBlock(btHistoryIndexBlock,String.format("%032d", currentStoragePlace ))) { //write sector A and B
                return false;
            }






        }catch(Exception ie){
            ie.printStackTrace();
        }


        return false;
    }

    public String bubblyCardIncrement(int iDecementValue) throws SmartCardException {

        // showImageSnap(R.drawable.classic);
        //tv.setText(" ");
        //showMessage("Card Detected : " + classic.getCardDetails().cardName, 'n');

        String strFinalRetMessage="";

        try {
            // classic.connect();

            // ***use below code only for Plus cards. It show how to switch from
            // Plus Security Level 1 to Security Level 3
            // byte[] switchKey = new byte[] { (byte) 0x00,
            // (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            // (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            // (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
            // };
            // try {
            // classic.switchToSL3(switchKey);//give sl3 switchKey
            // showMessage("Successfully switched to Plus Security Level 3. Tap again to continue "
            // , 'd');
            // } catch (AuthenticationException e) {
            //
            // e.printStackTrace();
            // } catch (GeneralSecurityException e) {
            //
            // e.printStackTrace();
            // } catch (SmartCardException e) {
            //
            // e.printStackTrace();
            // }
            // *** end of Plus variant code

            //showMessage("Uid :" + Utilities.dumpBytes(classic.getUID()), 'd');
            mCurCardUID = Utilities.dumpBytes(classic.getUID()).substring(2,10);
            Log.d(TAG,"UID: " + mCurCardUID);

            mCurrentKeyB = mCurCardUID;

            //String MFkey = "08" + MFSerTmp.Substring(0, 2) + MFSerTmp.Substring(6, 2) + "08" + MFSerTmp.Substring(4, 2) + MFSerTmp.Substring(2, 2);

            //Load Key
            //APDU Sent: FF 82 20 00 06
            // 08 9B 07 08 4F F4
            //APDU RECV: 90 00

            mCurrentKeyB = "08" + mCurCardUID.substring(0, 2) + mCurCardUID.substring(6, 8) + "08" + mCurCardUID.substring(4, 6) + mCurCardUID.substring(2, 4);

            classic.setTimeout(3000);
            //String buffRead = testClassicRead(0);
            //testClassicformat();
            //testClassicpersonalize();
            //testClassicupdateMasterKey();
            byte btSector = 2;
            strResult="";
            byte [] btREs = new byte[] {(byte)0x00, btSector};
            testBubblyauthenticate(mCurrentKeyB,(byte)0);

            if(AuthresGood) {
                strResult="";
                strResult = testClassicRead(0);
                strFinalRetMessage += "Sector 0 result:\n" + strResult;
                AuthresGood=false;
                testBubblyauthenticate(mCurrentKeyB, (byte)0);
                if(strResult.length()==0)
                    return "return read sec 0 failed";
                testBubblyauthenticate(mCurrentKeyB, (byte)0);
                if(AuthresGood) {

                    //public void decrement(int blockIndex,int value)
                    classic.increment(2,iDecementValue);
                    classic.transfer(2);

                    testBubblyauthenticate(mCurrentKeyB, (byte)0);

                    strResult="";
                    strResult = testClassicRead(btSector);

                    if(strResult.length()==0)
                        return "return read sec 2 failed";

                    iCardBalance = Integer.parseInt(strResult.substring(2,4),16);


                    strFinalRetMessage += "\nSector 2 result:\n" + strResult;


                    classic.close();


                    return strFinalRetMessage;
                }else{

                    classic.close();
                    return   "auth sec 2 fail";
                    //return strFinalRetMessage;
                }

            }else {
                strFinalRetMessage +=    "auth sec 0 fail";
                classic.close();
                return strFinalRetMessage;
            }




        } catch (IOException e) {

            showMessage("IOException: See Logcat!", 't');
            e.printStackTrace();
        }
        return "auth and read fail";
    }


    /** Classic Write IO Operations. */
    private void testClassicWrite() {
        byte[] bdata = null;
        boolean res = false;

        try {
            NxpLogUtils.d(TAG, "testClassicWrite, start");

            bdata = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };


            classic.write(bdata);
            res = true;
        } catch (SmartCardException e) {
            e.printStackTrace();
        }

        showMessage("Write :" + Utilities.dumpBytes(bdata), 'd');

        NxpLogUtils.d(TAG, "testClassicWrite, result is " + res);
        NxpLogUtils.d(TAG, "testClassicWrite, End");
    }

    /** Classic Read IO Operations. */
    private void testClassicRead() {
        byte[] read = null;
        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testClassicRead, start");
            read = classic.read(16);
            res = true;
        } catch (SmartCardException e) {
            e.printStackTrace();
        }
        showMessage("Read :" + Utilities.dumpBytes(read), 'd');
        NxpLogUtils.d(TAG, "testClassicRead, result is " + res);
        NxpLogUtils.d(TAG, "testClassicRead, End");
    }

    private String testClassicRead(int BlockNumber) {
        byte[] read = null;

        try {
            //Sample Sector 2 to read:
            //0A000000F5FFFFFF0A00000000FF00FF = 10 tokens *****####*****


            Log.d(TAG, "testClassicRead Sec:" + Integer.toString(BlockNumber) + " , start");
            read = classic.readBlock(BlockNumber);

            Log.d(TAG, "testClassicRead, Sec:= " + Integer.toString(BlockNumber) + " result is " + Utilities.dumpBytes(read));
            Log.d(TAG, "testClassicRead, End");
            return Utilities.dumpBytes(read);
        } catch (IOException e) {
            AuthresGood = false;
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG, "testClassicRead, Sec:= " + Integer.toString(BlockNumber) + " result is false");
            Log.d(TAG, "testClassicRead, End");
            Log.d(TAG, "Payfare_testclassic read io err:" + e.getMessage());
            Log.d(TAG, "\nPayFare_NXP_ReadBlock_IOErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + Integer.toString(BlockNumber));
            return "";
        }
        //showMessage("Read :" + Utilities.dumpBytes(read), 'd');

    }

    private boolean testClassicWriteBlock(int SectorNo,String strDataToWrite) {

        boolean res = false;
        byte [] bufferToWrite = null; //= new byte[16];


        try {


            bufferToWrite = HexStringToByteArray(strDataToWrite.substring(0,32));

            Log.d(TAG, "testClassicWrite Sec:" + Integer.toString(SectorNo) + " , start , with data:\n" + strDataToWrite);
            classic.writeBlock(SectorNo,bufferToWrite);
            classic.transfer(SectorNo);
            res=true;
            Log.d(TAG, "testClassicWrite, Sec:= " + Integer.toString(SectorNo) + " result is good" );
            Log.d(TAG, "testClassicWrite, End");
            return true;
        } catch (IOException e) {
            AuthresGood = false;
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG, "testClassicWrite, Sec:= " + Integer.toString(SectorNo) + " result is false");
            Log.d(TAG, "testClassicWrite, End");
            Log.d(TAG, "Payfare_testclassic writr io err:" + e.getMessage());
            Log.d(TAG, "\nPayFare_NXP_WriteBlock_IOErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + Integer.toString(SectorNo));
            return false;
        }catch(Exception ie){
            ie.printStackTrace();
            Log.d(TAG, "testClassicWrite, Sec:= " + Integer.toString(SectorNo) + " result is false");
            Log.d(TAG, "testClassicWrite, End");
            return false;
        }
        //showMessage("Read :" + Utilities.dumpBytes(read), 'd');

    }



    /** Classic Authenticate Operations. */
    private void testClassicauthenticate() {
        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testClassicauthenticate, start");
            byte sectorNo = 2;
            byte[] appId = new byte[] { 0x11, 0x11, 0x11 };
            byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF, (byte) 0xFF };

            classic.authenticate(sectorNo, appId, key);
            res = true;
        } catch (SmartCardException e) {
            e.printStackTrace();
        }
        showMessage("authenticate :" + res, 'd');
        NxpLogUtils.d(TAG, "testClassicauthenticate, result is " + res);
        NxpLogUtils.d(TAG, "testClassicauthenticate, End");
    }

    public void testClassicauthenticate(String strKeyB,byte btSectorNo) {
        String stackTrace;
        //Format String key into Bytes, then authenticate
        AuthresGood=false;
        try {
            NxpLogUtils.d(TAG, "testClassicauthenticate, start");
            byte sectorNo = btSectorNo;

            byte DefaultSector = (byte) 0x00;
            byte[] FactoryKey = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
            byte[] appId = new byte[]{0x11, 0x11, 0x11};

            byte[] key2 = {(byte) 0x08, (byte) 0x77, (byte) 0x4F, (byte) 0x08,
                    (byte) 0xB1, (byte) 0x9B};

            //08 8A 63 08 95 DA
            byte[] key999 = {(byte) 0x08, (byte) 0x8A, (byte) 0x63, (byte) 0x08, (byte) 0x95, (byte) 0xDA};

            byte[] key = HexStringToByteArray(strKeyB);

            byte[] key3 = Utilities.stringToBytes(strKeyB);
            //  if (key == key2 && key2 == key3) {

            Log.d(TAG, "Key format Verify is ok");
            // } else {
            //     Log.d(TAG, "Key format Verify is NOK");
            //  }

            //08 77 4F 08 B1 9B C++ key
            //08 77 4F 08 B1 9B key when built from previous code
            //[0,8,7,7,4,F,0,8,B,1,9,B, , , , ]
            //classic.authenticate(sectorNo, appId, key2);
            //089B0708EC62
            // classic.authenticateSectorWithKeyB(0, FactoryKey);
            classic.authenticateSectorWithKeyA(btSectorNo, FactoryKey);
            Log.d(TAG, "\nPayFare_NXP__Auth\nsec:" + Byte.toString(btSectorNo) + "  key:" + strKeyB + "AppID:111111 hex");
            AuthresGood = true;


        }catch(ClassicException e){
            AuthresGood=false;
            //String stackTrace = Log.getStackTraceString(e);
            //stackTrace = e.printStackTrace();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG,"\nPayFare_NXP_Failed_Auth_ClassicErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + Byte.toString(btSectorNo) + "  \nkey:" + strKeyB + "\nAppID:111111 hex");


        } catch (IOException e){
            AuthresGood=false;
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG,"\nPayFare_NXP_Failed_Auth_IOErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + Byte.toString(btSectorNo) + "  \nkey:" + strKeyB + "\nAppID:111111 hex");

        }
       /* catch (SmartCardException e) {
            AuthresGood=false;
            Log.d(TAG,"PayFare_NXP_Failed_Auth:\n" + e.getMessage() + "\nsec:" + Byte.toString(btSectorNo) + "  \nkey:" + strKeyB + "\nAppID:111111 hex");
            e.printStackTrace();
        }*/
        //showMessage("authenticate :" + AuthresGood, 'd');
        NxpLogUtils.d(TAG, "testClassicauthenticate, result is " + AuthresGood);
        NxpLogUtils.d(TAG, "testClassicauthenticate, End");
    }

    public void testBubblyauthenticate(String strKeyB,byte btSectorNo) {
        String stackTrace;
        //Format String key into Bytes, then authenticate
        AuthresGood=false;
        try {
            NxpLogUtils.d(TAG, "testBubblyauthenticate, start");
            byte sectorNo = btSectorNo;

            //byte[] FactoryKey = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

            // byte[] GreenOberthurCardKey = {(byte) 0x08, (byte) 0xCA, (byte) 0x0B, (byte) 0x08, (byte) 0xAB, (byte) 0x5A};
            byte[] key = HexStringToByteArray(strKeyB);

            classic.authenticateSectorWithKeyB(btSectorNo, key);
            Log.d(TAG, "\nBubbly_NXP__Auth\nsec:" + Byte.toString(btSectorNo) + "  key:" + strKeyB + "AppID:111111 hex");
            AuthresGood = true;


        }catch(ClassicException e){
            AuthresGood=false;
            //String stackTrace = Log.getStackTraceString(e);
            //stackTrace = e.printStackTrace();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG,"\nBubbly_NXP_Failed_Auth_ClassicErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + Byte.toString(btSectorNo) + "  \nkey:" + strKeyB + "\nAppID:111111 hex");


        } catch (IOException e){
            AuthresGood=false;
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG,"\nPayFare_NXP_Failed_Auth_IOErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + Byte.toString(btSectorNo) + "  \nkey:" + strKeyB + "\nAppID:111111 hex");

        }
       /* catch (SmartCardException e) {
            AuthresGood=false;
            Log.d(TAG,"PayFare_NXP_Failed_Auth:\n" + e.getMessage() + "\nsec:" + Byte.toString(btSectorNo) + "  \nkey:" + strKeyB + "\nAppID:111111 hex");
            e.printStackTrace();
        }*/
        //showMessage("authenticate :" + AuthresGood, 'd');
        NxpLogUtils.d(TAG, "testBubblyauthenticate, result is " + AuthresGood);
        NxpLogUtils.d(TAG, "testBubblyauthenticate, End");
    }

    public boolean testBubblyLogAuthenticate(int btSectorNo) {
        String stackTrace;
        //Format String key into Bytes, then authenticate
        AuthresGood=false;
        try {
            NxpLogUtils.d(TAG, "\ntestBubblyauthenticate, start");


            byte[] FactoryKey = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};


             classic.authenticateSectorWithKeyA(btSectorNo, FactoryKey);
            Log.d(TAG, "\nBubbly_NXP__Auth\n=Sector:" + String.valueOf(btSectorNo) + "  key:Factory ïs GOOD");
            AuthresGood = true;
            return AuthresGood;


        }catch(ClassicException e){
            AuthresGood=false;
            //String stackTrace = Log.getStackTraceString(e);
            //stackTrace = e.printStackTrace();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG,"\nBubbly_NXP_Failed_Auth_ClassicErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + String.valueOf(btSectorNo) + "  \nkey:Factory");
            return AuthresGood;

        } catch (IOException e){
            AuthresGood=false;
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            Log.d(TAG,"\nPayFare_NXP_Failed_Auth_IOErr:\n" + e.getMessage() + "\nStack:" + s + "sec:" + String.valueOf(btSectorNo)+ "  \nkey: Factory");
            return AuthresGood;
        }

    }


    /** Classic Update Master key Operations. */
    private void testClassicupdateMasterKey() {
        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testClassicupdateMasterKey, start");
            byte sectorNo = 2;
            byte[] oldKey = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
            byte[] newKey = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
            classic.updateApplicationMasterKey(sectorNo, oldKey, newKey);
            res = true;
        } catch (SmartCardException e) {
            e.printStackTrace();
        }

        showMessage("updateMasterKey : " + res, 'd');
        NxpLogUtils.d(TAG, "testClassicupdateMasterKey, result is " + res);
        NxpLogUtils.d(TAG, "testClassicupdateMasterKey, End");
    }

    /** Classic personalize Operations. */
    private void testClassicpersonalize() {
        byte sectorNo = 2;
        byte[] appId = new byte[] { 0x11, 0x11, 0x11 };
        byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF };

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testClassicpersonalize, start");
            classic.personalize(sectorNo, appId, key);
            res = true;
        } catch (SmartCardException e) {
            e.printStackTrace();
        }
        showMessage("persionalize :" + res, 'd');
        NxpLogUtils.d(TAG, "testClassicpersonalize, result is " + res);
        NxpLogUtils.d(TAG, "testClassicpersonalize, End");
    }

    /** Classic Format Operations. */
    private void testClassicformat() {
        byte sectorNo = 2;
        byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF };

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testClassicformat, start");
            classic.format(sectorNo, key);
            res = true;
        } catch (SmartCardException e) {

            e.printStackTrace();
        }
        showMessage("farmat :" + res, 'd');
        NxpLogUtils.d(TAG, "testClassicformat, result is " + res);
        NxpLogUtils.d(TAG, "testClassicformat, End");
    }

    /**
     * Plus lite operations.
     *
     * @throws ProtocolException
     *             when exception occur.
     * @throws SmartCardException
     *             when exception occur.
     */
    /*
    public void plusCardLogic() throws ProtocolException, SmartCardException {

        byte sectorNo = 8;
        byte[] appId = new byte[] { 0x1, 0x1, 0x8 };
        byte[] appKey = new byte[] { (byte) 0x11, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        byte[] newAppKey = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

        //showImageSnap(R.drawable.plus);
        tv.setText(" ");
        showMessage("Card Detected: " + plus.getCardDetails().cardName + " "
                + plus.getCardDetails().securityLevel, 'n');

        try {
            plus.connect();

            // this api will switch from plus sl0 to sl1. plus sl1 will be
            // detected as the classic card.
            // plus.personalizeCardToSL1();

            showMessage("UID: " + Utilities.dumpBytes(plus.getUID()), 'd');
            try {

                plus.personalizeSector(sectorNo, appId, appKey);
                NxpLogUtils.d(TAG, "Card personalize successful");
                byte[] testByte = new byte[] { (byte) 0x16, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0xE9, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0x16, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xFB,
                        (byte) 0x04, (byte) 0xFB, (byte) 0x21, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0xDE, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0x21, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0xFA,
                        (byte) 0x05, (byte) 0xFA, (byte) 0x2C, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0xD3, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF };
            //    testPlusWriteBlock(testByte);
           ///     testPlusReadBlock();
                plus.updateApplicationMasterKey(sectorNo, appId, appKey,
                        newAppKey);
                showMessage("Restore sector app key to factory default", 'n');
                showMessage("Performing write/read again", 'n');
                testByte = new byte[] { (byte) 0xFF, (byte) 0xAA, (byte) 0x00,
                        (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0x16, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x04, (byte) 0xFB, (byte) 0x04,
                        (byte) 0xFB, (byte) 0x21, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0xDE, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0x21, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x05, (byte) 0xFA, (byte) 0x05,
                        (byte) 0xFA, (byte) 0x2C, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0xD3, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF };
              //  testPlusWriteBlock(testByte);
             //   testPlusReadBlock();
                showCardDetails(plus.getCardDetails());
                NxpLogUtils.d(TAG, "Card key application change successful");

            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (SmartCardException e) {
                e.printStackTrace();
            }

            plus.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    /**
     * Mifare plus card write.
     *
     */
    private void PlusSL1CardLogic() {

        //showImageSnap(R.drawable.plus);
        tv.setText(" ");
        try {
            showMessage("Card Detected: " + plusSL1.getCardDetails().cardName
                    + " " + "Security Level 1", 'n');
        } catch (SmartCardException e1) {

            e1.printStackTrace();
        }
        showMessage("Uid :" + Utilities.dumpBytes(classic.getUID()), 'd');
        classic.setTimeout(2000);
        testClassicformat();
        testClassicpersonalize();
        testClassicupdateMasterKey();
        testClassicauthenticate();
        testClassicWrite();
        testClassicRead();
        // byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte)
        // 0xFF,
        // (byte) 0xFF, (byte) 0xFF };
        //
        // classic.authenticateSectorWithKeyA(0, key);
        // showCardDetails(classic.getCardDetails());
        try {
            plusSL1.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    /**
     * Mifare plus card write.
     *
     * @param testByte
     *            byte array.
     * @throws ProtocolException
     *             when exception occur.
     * @throws SmartCardException
     *             when exception occur.
     */
    /*
    private void testPlusWriteBlock(final byte[] testByte)
            throws ProtocolException, SmartCardException {
        boolean resp = false;

        try {
            plus.write(testByte);
            resp = true;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (PlusException e) {
            e.printStackTrace();
        } catch (SAMException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        showMessage("Write: " + resp, 'd');
    }
*/
    /**
     * Mifare plus read block.
     *
     * @throws ProtocolException
     *             when exception occur.
     * @throws SmartCardException
     *             when exception occur.
     * */
    /*
    private void testPlusReadBlock() throws ProtocolException,
            SmartCardException {
        // boolean resp = false ;
        byte[] read = null;
        try {
            read = plus.read(40);
            // resp = true;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (PlusException e) {
            e.printStackTrace();
        } catch (SAMException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        showMessage("Read Sector8: " + Utilities.dumpBytes(read), 'd');
    }
    */

    /**
     * creating the text record for NDEF Data.
     *
     * @param payload
     *            NDEF Data
     * @param locale
     *            locale
     * @param encodeInUtf8
     *            true/false
     * @return ndefrecord instance.
     */
    public static NdefRecord createTextRecord(final String payload,
                                              final Locale locale, final boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(
                Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
                .forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }

    /**
     * Mifare classic Card Logic.
     *
     * @throws SmartCardException
     */
    public void ultralightCardLogic() throws SmartCardException {

        // showImageSnap(R.drawable.ultralight);
        tv.setText(" ");
        showMessage("Card detected : " + mifareUL.getCardDetails().cardName,
                'n');

        try {
            mifareUL.connect();
            testULformat();
            testWriteNdef();
            testULreadNdef();
            showCardDetails(mifareUL.getCardDetails());
            mifareUL.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SmartCardException e) {

            e.printStackTrace();
        }
    }

    /**
     * Mifare Ultralight-C Card Logic.
     *
     * @throws SmartCardException
     */
    protected void ultralightcCardLogic() throws SmartCardException {

        // showImageSnap(R.drawable.ultralight_c);
        tv.setText(" ");
        showMessage("Card Detected : " + objUlCardC.getCardDetails().cardName,
                'n');

        byte[] data;

        try {
            objUlCardC.connect();
            data = objUlCardC.readAll();
            showMessage("Read All o/p is : " + Utilities.dumpBytes(data), 'd');
            showCardDetails(objUlCardC.getCardDetails());
        } catch (IOException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("IOException occured... check LogCat", 't');
            e.printStackTrace();
        } catch (SmartCardException e) {

            e.printStackTrace();
        }

    }

    /**
     * Mifare Ultralight EV1 CardLogic.
     *
     * @throws SmartCardException
     */
    protected void ultralightEV1CardLogic() throws SmartCardException {

        //showImageSnap(R.drawable.ultralight_ev1);
        tv.setText(" ");
        String str = "Card Detected : "
                + objUlCardEV1.getCardDetails().cardName;
        showMessage(str, 'n');

        byte[] data;
        try {
            /** connect to card, authenticate and read data */
            objUlCardEV1.connect();
            data = objUlCardEV1.readAll();
            data = objUlCardEV1.read(DEFAULT_PAGENO_ULTRALIGHT);

            str = Utilities.dumpBytes(data);
            showMessage("Data read from card @ " + "page "
                    + DEFAULT_PAGENO_ULTRALIGHT + " is " + str, 'd');

            byte[] bytesData = DATA.getBytes();
            String s1 = new String(bytesData);
            showMessage("Input String is: " + s1, 'd');
            byte[] bytesEncData = encryptAESData(bytesData, bytesKey);
            str = "Enctrypted string is " + Utilities.dumpBytes(bytesEncData);
            showMessage(str, 'd');

            objUlCardEV1.write(4, Arrays.copyOfRange(bytesEncData, 0, 4));
            objUlCardEV1.write(5, Arrays.copyOfRange(bytesEncData, 4, 8));
            objUlCardEV1.write(6, Arrays.copyOfRange(bytesEncData, 8, 12));
            objUlCardEV1.write(7, Arrays.copyOfRange(bytesEncData, 12, 16));

            byte[] bytesDecData = decryptAESData(data, bytesKey);
            String s = new String(bytesDecData);
            str = "Decrypted string is " + s;
            showMessage(str, 'd');

            if (Arrays.equals(bytesData, bytesDecData)) {
                showMessage("Matches", 't');
            }

            showCardDetails(objUlCardEV1.getCardDetails());
        } catch (IOException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("IOException occured... check LogCat", 't');
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("InvalidKeyException occured... check LogCat", 't');
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("NoSuchAlgorithmException occured... check LogCat", 't');
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("NoSuchPaddingException occured... check LogCat", 't');
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("IllegalBlockSizeException occured ... check LogCat",
                    't');
            e.printStackTrace();
        } catch (BadPaddingException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("BadPaddingException occured ... check LogCat", 't');
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            showMessage(e.getMessage(), 'l');
            showMessage("InvalidAlgorithmParameterException ... check LogCat",
                    't');
            e.printStackTrace();
        } catch (SmartCardException e) {

            e.printStackTrace();
        }
		/* Save the logs in \sdcard\NxpLogDump\logdump.xml */
        NxpLogUtils.save();
    }

    /** Ultralight Read Ndef Operations. */
    private void testULreadNdef() {

        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testULreadNdef, start");
            NdefMessage msgread = null;
            msgread = mifareUL.readNdef();
            String sMsg = new String(msgread.getRecords()[0].getPayload());
            res = true;
            showMessage(
                    "Read NDEF Data: "
                            + Utilities.dumpHexAscii(sMsg.getBytes()), 'd');
            NxpLogUtils.i(TAG,
                    Utilities.dumpBytes(msgread.getRecords()[0].getPayload()));
            NxpLogUtils.i(TAG, sMsg);

        } catch (SmartCardException e) {
            showMessage("Read NDEF: " + res, 'd');
            e.printStackTrace();
        }

        NxpLogUtils.d(TAG, "testULreadNdef, result is " + res);
        NxpLogUtils.d(TAG, "testULreadNdef, End");
    }

    /** Ultralight Format Operations. */
    private void testULformat() {


        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testULformat, start");
            mifareUL.format();
            res = true;
            showMessage("Format: " + res, 'd');
        } catch (SmartCardException e) {
            showMessage("Format: " + res, 'd');
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testULformat, result is " + res);
        NxpLogUtils.d(TAG, "testULformat, End");
    }

    /** Ultralight write ndef Operations. */
    public void testWriteNdef() {
        NdefRecord textRecord = createTextRecord(
                "MIFARE SDK by NXP Semiconductors Inc.", Locale.ENGLISH, true);
        NdefMessage message = new NdefMessage(new NdefRecord[] { textRecord });

        boolean res = false;

        try {

            NxpLogUtils.d(TAG, "testWriteNdef, start");
            mifareUL.write(message);
            res = true;
            showMessage("NDEF - Create Text Record: " + res, 'd');
        } catch (SmartCardException e) {
            showMessage("NDEF - Create Text Record: " + res, 'd');
            e.printStackTrace();
        }

        NxpLogUtils.d(TAG, "testWriteNdef, result is " + res);
        NxpLogUtils.d(TAG, "testWriteNdef, End");

    }

    /**
     * Update the card image on the screen.
     *
     * @param cardTypeId
     *            resource image id of the card image
     *
     */

    private void showImageSnap(final int cardTypeId) {

        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mImageView.getLayoutParams().width = (size.x * 2) / 3;
        mImageView.getLayoutParams().height = size.y / 3;
        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            public void run() {
                mImageView.setImageResource(cardTypeId);
            }
        }, 200);

        mImageView.setImageResource(R.drawable.mifare_p);*/
    }

    /**
     * This will display message in toast or logcat .
     *
     * @param str
     *            String to be logged or displayed
     * @param where
     *            't' for Toast; 'l' for Logcat; 'd' for Display in UI; 'n' for
     *            logcat and textview 'a' for All
     *
     */
    protected void showMessage(final String str, final char where) {

        switch (where) {

            case 't':
                // Toast.makeText(MainLiteActivity.this, "\n" + str,
                // Toast.LENGTH_SHORT).show();
                break;
            case 'l':
                NxpLogUtils.i(TAG, "\n" + str);
                break;
            case 'd':
                tv.setText(tv.getText() + "\n-----------------------------------\n"
                        + str);
                break;
            case 'a':
                //Toast.makeText(MainLiteActivity.this, "\n" + str,
                // Toast.LENGTH_SHORT).show();
                NxpLogUtils.i(TAG, "\n" + str);
                tv.setText(tv.getText() + "\n-----------------------------------\n"
                        + str);
                break;
            case 'n':
                NxpLogUtils.i(TAG, "Dump Data: " + str);
                tv.setText(tv.getText() + "\n-----------------------------------\n"
                        + str);
                break;
            default:
                break;
        }
        return;
    }

    /**
     * Encrypt the supplied data with key provided.
     *
     * @param data
     *            data bytes to be encrypted
     * @param key
     *            Key to encrypt the buffer
     * @return encrypted data bytes
     * @throws NoSuchAlgorithmException
     *             NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     *             NoSuchPaddingException
     * @throws InvalidKeyException
     *             InvalidKeyException
     * @throws IllegalBlockSizeException
     *             IllegalBlockSizeException
     * @throws BadPaddingException
     *             eption BadPaddingException
     * @throws InvalidAlgorithmParameterException
     *             InvalidAlgorithmParameterException
     */
    protected byte[] encryptAESData(final byte[] data, final byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {
        final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encdata = cipher.doFinal(data);
        return encdata;
    }


    protected byte[] decryptAESData(final byte[] encdata, final byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {

        final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        byte[] decdata = cipher.doFinal(encdata);
        return decdata;
    }


}