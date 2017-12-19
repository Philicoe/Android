package bubbly2017.payfare.com.bubbly2017;

/**
 * Created by philip on 2017/11/02.
 */

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class NsdHelper {

    public static Context mContext;

    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;

    public myFile_Logger Log_File = new myFile_Logger();
    public File logFileDir;
    public String filePath="";
    public boolean enableLog = false;
    public String logFileText ="";
    public static final String SERVICE_TYPE = "_http._tcp.";

    public static final String TAG = "PayFare_NsdHelper";
    public String mServiceName = "Payfare";

    NsdServiceInfo mService;

    public NsdHelper(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        // SaveLogLine("\nNSD SetManager",mContext);
    }

    public void initializeNsd() {
        initializeResolveListener();
        initializeDiscoveryListener();
        initializeRegistrationListener();

        //SaveLogLine("\nNSD initializeNsd Done.",mContext);
        //mNsdManager.init(mContext.getMainLooper(), this);

    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                //SaveLogLine("\nNSD Service discovery started",mContext);
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success" + service);
                // SaveLogLine("\nNSD Service discovery success",mContext);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    //SaveLogLine("\n\"Unknown Service Type: \" + service.getServiceType()",mContext);
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(mServiceName)) {
                    // SaveLogLine("\nSame machine: " + mServiceName,mContext);
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (service.getServiceName().contains(mServiceName)){
                    //SaveLogLine("\nResolve Service" + service,mContext);
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                //SaveLogLine("\nservice lost" + service,mContext);
                if (mService == service) {
                    mService = null;
                }
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
                //SaveLogLine("\nDiscovery stopped: " + serviceType,mContext);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery start failed: Error code:" + errorCode);
                // SaveLogLine("\nDiscovery start failed: Error code:" + errorCode,mContext);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery stop failed: Error code:" + errorCode);
                //SaveLogLine("\nDiscovery stop failed: Error code:" + errorCode,mContext);
                mNsdManager.stopServiceDiscovery(this);
            }
        };

    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "\nResolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                mService = serviceInfo;
            }
        };
        // SaveLogLine("\nNSD Resolve Listener Done.",mContext);
    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                mServiceName = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            }

        };
        SaveLogLine("\nNSD Resgitration Listener Done.",mContext);
    }

    public void registerService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);

        //SaveLogLine("\nNSD Port:" + Integer.toString(port),mContext);
        //SaveLogLine("\nNSD Serv:" + mServiceName,mContext);
        //SaveLogLine("\nNSD SerT:" + SERVICE_TYPE.toString(),mContext);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

        //SaveLogLine("\nNSD reg done.." + Integer.toString(port),mContext);

    }

    public void SaveLogLine(String strLine,Context theContext){

        /*
          if(Log_File.isExternalStorageWritable()){

              logFileDir = Log_File.getAlbumStorageDir("PayFare");
              filePath = logFileDir + "/MyAppLog.txt";
              if(Log_File.CreateAppFile(filePath,mContext)==true){
                  Log_File.SaveALine(filePath,strLine,theContext);

              }

          }*/

        return;
    }


    public String getLogLine(){
        return  logFileText;
    }
    public void discoverServices() {
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        // SaveLogLine("\nDiscovering Services.",mContext);
    }

    public void stopDiscovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
    }
}