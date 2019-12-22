package interlogic.gr.flutter_cblue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.ParcelUuid;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;


/** FlutterCbluePlugin */
public class FlutterCbluePlugin implements FlutterPlugin, MethodCallHandler {




  private OutputStream outputStream;
  private InputStream inStream;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_cblue");
    channel.setMethodCallHandler(new FlutterCbluePlugin());
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_cblue");
    channel.setMethodCallHandler(new FlutterCbluePlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);

    } else if (call.method.equals("printToBt")) {

      String printStr = call.argument("printStr");

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();


        if (bluetooth != null) {

          if (bluetooth.isEnabled()) {

            Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();


            if (pairedDevices.size() > 0) {
              // There are paired devices. Get the name and address of each paired device.
              for (BluetoothDevice device : pairedDevices) {

                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                String dev = deviceName + "-" + deviceHardwareAddress;
                if (deviceName.startsWith("zebra")) {
                  bluetooth.cancelDiscovery();

                  ParcelUuid[] uuids = device.getUuids();

                  try {

                    final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();

                    write(printStr);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                        //Do something after 100ms
                        try {
                          socket.close(); //Socket closed
                        } catch (IOException e) {
                          //Toast.makeText(ListFine.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                      }
                    }, 10000);


                  } catch (IOException e) {
                    //Toast.makeText(ListFine.this, e.getMessage(), Toast.LENGTH_LONG).show();
                  }
                  //Toast.makeText(ListFine.this, "Γίνεται εκτύπωση στον εκτυπωτή: " + deviceName, Toast.LENGTH_LONG).show();


//                  Intent returnIntent = new Intent();
//                  setResult(Activity.RESULT_OK, returnIntent);
//
//                  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                  //finish();


                }
              }
            }


//          } else {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//
//            //status = "You must enable Bluetooth.";
//
//
//
//          //Toast.makeText(MainActivity.this, status, Toast.LENGTH_LONG).show();


            result.success("Printed Successfully!");



        }
      }


    } else {
      result.notImplemented();
    }
  }

  public void write(String s) throws IOException {
    outputStream.write(s.getBytes());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
