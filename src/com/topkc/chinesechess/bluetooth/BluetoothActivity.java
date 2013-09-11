package com.topkc.chinesechess.bluetooth;

import com.topkc.chinesechess.R;
import com.topkc.chinesechess.util.TabMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ������Ϸ����Main����
 * @author WEI
 *
 */
public class BluetoothActivity extends Activity{
	
    //View
    View joinGameButton = null;
    View createGameButton = null;
    
    View tableLayout = null;  //��ʾ��ǰ��ҵı��
    TextView tableLayout_GameTitle = null;  //�������Ǳ���е���������
    TextView tableLayout_CreateGameMan = null;   //������Ϸ��ұ������Ϸ�����ߵ���Ϣ
    TextView tableLayout_JoinGameMan = null; //
    View startGameButton = null;  //���˶�������Ϸ�󣬿�ʼ��Ϸ��ť
    
    

    
    
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;
    
    //private final Handler mHandler = null;
    
	//TabMenu ����
	TabMenu.MenuBodyAdapter[] bodyAdapter = new TabMenu.MenuBodyAdapter[2];// []��ʾ�ж�����ѡ��
	TabMenu.MenuTitleAdapter titleAdapter;
	TabMenu tabMenu;
	int selTitle = 0;

	BluetoothChessView gameView = null;
	LinearLayout gameViewLayout = null;
	//View
	ImageView gameStatus = null;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//������Ϸȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.bluetooth_activity);
		
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        //�����̳�ʼ��
		gameView = new BluetoothChessView(this, this);
        
        //��ʼ����ť��������¼�
        createGameButton = (Button)findViewById(R.id.Button01);
        joinGameButton = (Button)findViewById(R.id.Button02);
        
        tableLayout = (TableLayout)findViewById(R.id.TableLayout01);
        tableLayout_GameTitle = (TextView)findViewById(R.id.TextView01_GameTitle);
        tableLayout_CreateGameMan = (TextView)findViewById(R.id.TextView02_CreateGameMan);
        tableLayout_JoinGameMan = (TextView)findViewById(R.id.TextView03_JoinGameMan);
        startGameButton = (Button)findViewById(R.id.Button03_StartGame);
        
        createGameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BluetoothChess.isService = true ;	//���õ�ǰ�û�Ϊ��������
				
				//���������ɱ������豸����
				if (mBluetoothAdapter.getScanMode() !=
		            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
		            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		            startActivity(discoverableIntent);
		        }
				//�Ѵ�����Ϸ�ͼ�����Ϸ�˵� ����
				createGameButton.setVisibility(View.GONE);joinGameButton.setVisibility(View.GONE);
				//����Ϸ�����Ϣ��� �� ��ʼ��ť  ����Ϊ�ɼ�
				tableLayout.setVisibility(View.VISIBLE);startGameButton.setVisibility(View.VISIBLE);
				//���������Ϣ��������Ϣ
				tableLayout_GameTitle.setText("�����Ϣ(��������)");

				//���豸������,��������б�
				String nameAddress = mBluetoothAdapter.getName();
				tableLayout_CreateGameMan.setText(nameAddress);
				
			}
		});
        
        joinGameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BluetoothChess.isService = false;
				//ת���豸�б�Activity��������ɺ�ص�
		        Intent serverIntent = new Intent(BluetoothActivity.this, DeviceListActivity.class);
		        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			}
		});
        
        startGameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(BluetoothChess.isService){//�����ǰΪ��������
					if(BluetoothChess.isJoinManReady){
						String message = BluetoothChess.START_GAME;
				        sendMessage(message);
						initGame();
						
						changePlayerImage(BluetoothChess.RED_PLAYER);//�����������壬��Ϊ��ɫ��ʶ
					}else{
						Toast.makeText(BluetoothActivity.this, "��һ�û��׼����", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					//if(!BluetoothChess.isJoinManReady){
						BluetoothChess.isJoinManReady = true;//�ͻ����Ѿ�׼��
						String joinDeviceName = tableLayout_JoinGameMan.getText().toString()+"--��׼��";
						tableLayout_JoinGameMan.setText(joinDeviceName);//���豸������,��������б�
						
						//�������������Ϣ���Լ��Ѿ�׼������
						String message = BluetoothChess.IS_READY;
				        sendMessage(message);
					//}
					
					//������Ϸ�ͷ���
					//Intent intent = new Intent();
					//intent.setClass(BluetoothActivity.this, BluetoothChessClient.class);
					//BluetoothActivity.this.startActivity(intent);
				}
				
			}
		});
        
	}
	
	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void restartGame(){
		if(BluetoothChess.isService){
			String message = BluetoothChess.START_GAME;
	        sendMessage(message);
	        gameView = new BluetoothChessView(this, this);
			initGame();//��ʼ��Ϸ
			
			changePlayerImage(BluetoothChess.RED_PLAYER);//�����������壬��Ϊ��ɫ��ʶ
		}else{
			Toast.makeText(BluetoothActivity.this, "��֪ͨ�����������¿�ʼ��Ϸ��", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void changePlayerImage(int player ){
		if(player == BluetoothChess.RED_PLAYER){
			BluetoothChess.isRedTrun = true;//�������£����ı��ʶ״̬
			Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
			gameStatus.setImageBitmap(gameStatusBitmap);
		}else{
			BluetoothChess.isRedTrun = false;//�������£����ı��ʶ״̬
			Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_black);
			gameStatus.setImageBitmap(gameStatusBitmap);
		}
		
	}
	/**
	 * ��ȥ������Ϸʱ ��ʾ������ 
	 */
    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
        	if (mChatService == null) setupChat();
        }
    }
    
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
    
    
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        //mConversationView = (ListView) findViewById(R.id.in);
       // mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
       // mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        //mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
       // mSendButton = (Button) findViewById(R.id.button_send);
       // mSendButton.setOnClickListener(new OnClickListener() {
       //     public void onClick(View v) {
                // Send a message using content of the edit text widget
       //         TextView view = (TextView) findViewById(R.id.edit_text_out);
        //        String message = view.getText().toString();
        //        sendMessage(message);
        //    }
        //});

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
           // mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    
    
    //The Handler that gets information back from the BluetoothChatService
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                /**
                //�ı���ʾ��ǰ��Ϸ��״̬
                case BluetoothChatService.STATE_CONNECTED:
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    mTitle.setText(R.string.title_not_connected);
                    break;
                */
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
            
                readBluetoothMessage(readMessage);//�����������յ�����Ϣ
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                //�ж��Ƿ�ͻ���
                if(BluetoothChess.isService){
    				
    				tableLayout_JoinGameMan.setText(mConnectedDeviceName);//���豸������,��������б�
                }
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
           
            //2011��5��14��23:48:52
            case BluetoothChess.SEND_CHESS_MOVE:
                // save the connected device's name
                String mChessMove = msg.getData().getString("MOVE_KEY");
                //Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                
                System.out.println("===mChessMove:"+mChessMove);
                //������Ϣ
                sendChessMoveMessage(mChessMove);
                
                break;
            case BluetoothChess.CHANGE_IMAGE_TO_RED:
            	//BluetoothChess.isRedTrun = true;
            	Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
    			gameStatus.setImageBitmap(gameStatusBitmap);
                break;
            case BluetoothChess.CHANGE_IMAGE_TO_BLACK:
            	//BluetoothChess.isRedTrun = false;
            	Bitmap gameStatusBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_black);
    			gameStatus.setImageBitmap(gameStatusBitmap2);
                break;
            case BluetoothChess.RESTART_GAME:
            	//BluetoothChess.isRedTrun = false;
            	restartGame();//���¿�ʼ��Ϸ
                break;
            }
            
        }
    };
    
    /**
     * �������岽����Ϣ
     * @param msg
     */
    public void sendChessMoveMessage(String msg){
    	if(BluetoothChess.isService){
    		msg = BluetoothChess.CHESS_MOVE +"-"+  BluetoothChess.SERVER +"-"+  msg;
    	}else{
    		msg = BluetoothChess.CHESS_MOVE +"-"+  BluetoothChess.CLIENT +"-"+  msg;
    	}
    	sendMessage(msg);
    }
    /**
     * ���յ�������Ϣ����
     */
    public void readBluetoothMessage(String readMessage){
    	System.out.println(readMessage);
    	if(BluetoothChess.isService){
    		//�յ��ͻ���׼������Ϣ
    		if(readMessage.endsWith(BluetoothChess.IS_READY)||readMessage == BluetoothChess.IS_READY){
    			BluetoothChess.isJoinManReady = true;//�ͻ����Ѿ�׼��
				String joinDeviceName = tableLayout_JoinGameMan.getText().toString()+"--��׼��";
				tableLayout_JoinGameMan.setText(joinDeviceName);//���豸������,��������б�
          	}
    	}else {
    		//�յ���ʼ��Ϸ����Ϣ
    		if(readMessage.endsWith(BluetoothChess.START_GAME)||readMessage == BluetoothChess.START_GAME){
    			if(BluetoothChess.isJoinManReady){
    				gameView = new BluetoothChessView(this, this);
    				initGame();//��ʼ��Ϸ
    				changePlayerImage(BluetoothChess.BLACK_PLAYER); //�ڿͷ��� ���ں�������
    			}
          	}
    	}
    	
    	String[] msgStr=readMessage.split("-");
    	/*
		for(int i=0;i<msgStr.length;i++){
			System.out.println(msgStr[i]);
		}
		*/
		if(msgStr.length >=6){
			if(msgStr[0].endsWith(BluetoothChess.CHESS_MOVE)){
				String whoChessMan = msgStr[1];
				//�ѷ��͹����ĺ������귭��ɺ�������
				int fromX = 9-Integer.parseInt(msgStr[2]);
				int fromY = 8-Integer.parseInt(msgStr[3]);
				int toX = 9-Integer.parseInt(msgStr[4]);
				int toY = 8-Integer.parseInt(msgStr[5]);
				
				Message msg = gameView.mHandler.obtainMessage(BluetoothChess.MOVE_TO);
		        Bundle bundle = new Bundle();
		        bundle.putInt("fromX", fromX);
		        bundle.putInt("fromY", fromY);
		        bundle.putInt("toX", toX);
		        bundle.putInt("toY", toY);
		        msg.setData(bundle);
		        gameView.mHandler.sendMessage(msg);
			}
		}
		

        //gameView.mHandler.sendEmptyMessage(1);
    }
    
    //startActivityForResult�ص�����
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                
                //System.out.println("--Device:"+device.getName());
                Toast.makeText(this, "Device:"+device.getName(), Toast.LENGTH_SHORT).show();
                // Attempt to connect to the device
                mChatService.connect(device);
                joinBluetoothGame(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                //setupChat();
            } else {
                // User did not enable Bluetooth or an error occured
                //Log.d(TAG, "BT not enabled");
                //Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }
    
    /**
     * ����Device List Activity ѡ����豸������Ϸ
     * @param device
     */
    public void joinBluetoothGame(BluetoothDevice device){
    	//�Ѵ�����Ϸ�ͼ�����Ϸ�˵� ����
		createGameButton.setVisibility(View.GONE);joinGameButton.setVisibility(View.GONE);
		//����Ϸ�����Ϣ��� �� ��ʼ��ť  ����Ϊ�ɼ�
		tableLayout.setVisibility(View.VISIBLE);startGameButton.setVisibility(View.VISIBLE);
		//���������Ϣ��������Ϣ
		tableLayout_GameTitle.setText("�����Ϣ(�ͻ���)");
		
		//���豸������
		String nameAddress = mBluetoothAdapter.getName();
		tableLayout_CreateGameMan.setText(device.getName());
		tableLayout_JoinGameMan.setText(nameAddress);
    }
    
    public void initGame(){
    	
    	setContentView(R.layout.bluetooth_chess_activity);
    	
    	//��ʼ����Ϸ����Ĳ����ļ�
		gameViewLayout = (LinearLayout) findViewById(R.id.BluetoothGameViewActivity);
		
		//�����̳�ʼ��
		//gameView = new BluetoothChessView(this, this);
		gameView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 686));//630
		gameViewLayout.addView(gameView);
		
		//����ImageButton NewGame �� Back ��ť
		LinearLayout buttonLinearLayout = new LinearLayout(this);//��ť�ǲ��ֵĺ��򲼾�
		RelativeLayout.LayoutParams imageButtonLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		buttonLinearLayout.setPadding(5, 5, 5, 8);
		//NewGame��ť
		ImageButton imageButton01 = new ImageButton(this);
		imageButton01.setBackgroundResource(R.drawable.image_button_newgame);
		buttonLinearLayout.addView(imageButton01, imageButtonLayoutParams);
		//GameStatus
		gameStatus = new ImageView(this);
		Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
		gameStatus.setId(123456);
		gameStatus.setImageBitmap(gameStatusBitmap);
		//gameStatus.setBackgroundColor(Color.BLUE);
		buttonLinearLayout.addView(gameStatus, imageButtonLayoutParams);
		//Back��ť
		ImageButton imageButton02 = new ImageButton(this);
		imageButton02.setBackgroundResource(R.drawable.image_button_back);
		buttonLinearLayout.addView(imageButton02, imageButtonLayoutParams);
		
		gameViewLayout.addView(buttonLinearLayout);
		
		imageButton01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(BluetoothActivity.this); 
		        //builder.setMessage("ȷ��Ҫ�˳���?"); 
		        builder.setTitle("ȷʵҪ���¿�ʼ��"); 
		        
		        builder.setPositiveButton("ȷ��", 
		                new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								restartGame();//���¿�ʼ��Ϸ
							}
						});
		        builder.setNegativeButton("ȡ��", 
		                new android.content.DialogInterface.OnClickListener() { 
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        dialog.dismiss(); 
		                    } 
		                }); 
		        builder.create().show();
		        /*
				AlertDialog.Builder builder = new AlertDialog.Builder(.this); 
				builder.setMessage("ȷ�����¿�ʼ?") 
				       .setCancelable(false) 
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
				           public void onClick(DialogInterface dialog, int id) { 
				        	   restartGame();
				           } 
				       }) 
				       .setNegativeButton("No", new DialogInterface.OnClickListener() { 
				           public void onClick(DialogInterface dialog, int id) { 
				                dialog.cancel(); 
				           } 
				       }); 
				AlertDialog alert = builder.create(); 
				*/
			}
		});
		imageButton02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_black);
				//gameStatus.setImageBitmap(gameStatusBitmap);
				//String message = "----This is test Bluetooth connection----";
		        //sendMessage(message);
				Toast.makeText(BluetoothActivity.this, "��׼���壡лл��", Toast.LENGTH_SHORT).show();
		        
			}
		});
		
		//�趨����TabMenu�ı��������
        this.MenuContentSetting();
    }
    

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//�����ֻ����˰���
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
			exitDialog();
            return false; 
        } 
		return super.onKeyDown(keyCode, event);
	}
	
	/**
     * ����Ϸ���������˰�ťʱ����ʾ�˳���
     */
    protected void exitDialog() { 
        AlertDialog.Builder builder = new Builder(this); 
        builder.setMessage("ȷ��Ҫ�˳���?"); 
        builder.setTitle("��ʾ"); 
        
        builder.setPositiveButton("�������˵�", 
                new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						/*
						Intent intent = new Intent();
						intent.setClass(GameViewActivity.this, ChineseChessActivity.class);
						GameViewActivity.this.startActivity(intent);
						*/
					}
				});
		/*
        builder.setNeutralButton("ȷ��", 
                new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		*/
        builder.setNegativeButton("������Ϸ", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    } 
    
    
    
	/**
	 * TabMenu������������Ե��趨
	 */
	public void MenuContentSetting(){
		// ���÷�ҳ���ı���
		titleAdapter = new TabMenu.MenuTitleAdapter(this, new String[] { "����",
				"����" }, 16, 0xFF222222, Color.LTGRAY, Color.WHITE);		
		
		// ����ÿ���ҳ��������
		bodyAdapter[0] = new TabMenu.MenuBodyAdapter(this, new String[] { "����",
				"�ļ�", "ˢ��", "�˳�" }, new int[] { R.drawable.menu_search,
				R.drawable.menu_filemanager, R.drawable.menu_refresh,
				R.drawable.menu_quit }, 13, 0xFFFFFFFF);
		bodyAdapter[1] = new TabMenu.MenuBodyAdapter(this, new String[] { "��ַ",
				"��ǩ", "����" }, new int[] { R.drawable.menu_inputurl,
				R.drawable.menu_bookmark,R.drawable.menu_downmanager  }, 13,
				0xFFFFFFFF);
		tabMenu = new TabMenu(this, new TitleClickEvent(),
				new BodyClickEvent(), titleAdapter,
				// 0x55123456,//TabMenu�ı�����ɫ
				0xDD000000, R.style.PopupAnimation);// ��������ʧ�Ķ���
		tabMenu.update();
		tabMenu.SetTitleSelect(0);
		tabMenu.SetBodyAdapter(bodyAdapter[0]);
	}
	
	/**
	 * TabMenu titleClick �����¼�������
	 * @author WEI
	 *
	 */
	class TitleClickEvent implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			selTitle = arg2;
			tabMenu.SetTitleSelect(arg2);
			tabMenu.SetBodyAdapter(bodyAdapter[arg2]);
		}
	}
	/**
	 * TabMenu bodyClick �����¼�������
	 * @author WEI
	 *
	 */
	class BodyClickEvent implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			tabMenu.SetBodySelect(arg2, Color.GRAY);
			
			if(selTitle==0 && arg2==3){//��1������4��
				finish();
			}
			//Toast.makeText(ChineseChessActicity.this, str, 500).show();
		}
	}

	/**
	 * TabMenu ����MENU onCreateOptionsMenu()
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// ���봴��һ��
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * TabMenu ����MENU onMenuOpened()
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (tabMenu != null) {
			if (tabMenu.isShowing())
				tabMenu.dismiss();
			else {
				tabMenu.showAtLocation(findViewById(R.id.BluetoothGameViewActivity),
						Gravity.BOTTOM, 0, 0);
			}
		}
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}
}
