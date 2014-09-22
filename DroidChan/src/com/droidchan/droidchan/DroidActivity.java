package com.droidchan.droidchan;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import fragments.ForumFragment;
import fragments.GroupFragment;
import fragments.ProfileFragment;
import fragments.SuscriptionFragment;

public class DroidActivity extends Activity {

	private SharedPreferences preferences;
	private ActionBar abar;
	private String login;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_droid);
		
		//Se cambia el nombre de la actividad
		setTitle("DroidChan");
		
		initActionBar();
	        
		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);        
			
		//recupero los datos de la sesion:
		initSessionData();	
	}
	
	private void initSessionData(){
		login = preferences.getString("login", "");
		password = preferences.getString("password", "");
		//si no tengo nada, a la pagina de logeo:
		if(login==null || password==null){
			//Vacío las sharedpreferences
			SharedPreferences.Editor editorPreferences = preferences.edit();
			editorPreferences.clear();
			editorPreferences.commit();
			//Salto a la pagina de logeo
			Intent intent = new Intent(this.getApplicationContext(),MainActivity.class);
			startActivityForResult(intent,0);
		}
	}
	
	private void initActionBar(){
		//Obtenemos una referencia a la actionbar
	    abar = getActionBar();
	    
	    //Establecemos el modo de navegación por pestañas
	    abar.setNavigationMode(
	        ActionBar.NAVIGATION_MODE_TABS);
	 
	    //Ocultamos si queremos el título de la actividad
	    //abar.setDisplayShowTitleEnabled(false);
	 
	    //Creamos las pestañas
	    ActionBar.Tab tab1 =
	        abar.newTab().setText("Inicio");
	    
	    
	    ActionBar.Tab tab2 =
	        abar.newTab().setText("Perfil");
	    
	    ActionBar.Tab tab3 =
		    abar.newTab().setText("Grupos");
	    
	    ActionBar.Tab tab4 =
			    abar.newTab().setText("Suscripciones");
//	    
	    //Creamos los fragments de cada pestaña
	        Fragment tab1frag = new ForumFragment();
	        Fragment tab2frag = new ProfileFragment();
	        Fragment tab3frag = new GroupFragment();
	        Fragment tab4frag = new SuscriptionFragment();
	        
	        //Asociamos los listener a las pestañas
	        tab1.setTabListener(new TabListener(tab1frag));
	        tab2.setTabListener(new TabListener(tab2frag));
	        tab3.setTabListener(new TabListener(tab3frag));
	        tab4.setTabListener(new TabListener(tab4frag));
	        //Añadimos las pestañas a la action bar
	        abar.addTab(tab1);
	        abar.addTab(tab2);
	        abar.addTab(tab3);
	        abar.addTab(tab4);
	}
	
	//Infla en menú
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.droid_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
//	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId())
	    {
	        case R.id.action_new_thread:
	            Toast.makeText(this, "CREATE THREAD", Toast.LENGTH_SHORT).show();
//	            Intent intent = new Intent(this.getApplicationContext(),MainActivity.class);
//				startActivityForResult(intent,0);
	            Intent intent = new Intent(this, CreateThreadActivity.class);
				startActivityForResult(intent,0);
	            break;
//	        case R.id.action_search:
//	            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
//	            break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	 
	    return true;
	}
//	
//	private AlertDialog getPhotoDialog() {
//        AlertDialog _photoDialog = null;
//		if (_photoDialog == null) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            builder.setTitle(R.string.photo_source);
//            builder.setTitle("TITULO");
//         
//            builder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
// 
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(
//                            "android.media.action.IMAGE_CAPTURE");
//                    File photo = null;
//                    try {
//                        // place where to store camera taken picture CHANGE TO CREATETHREADACTIVITY
//                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", DroidActivity.this);
//                        photo.delete();
//                    } catch (Exception e) {
//                        Log.v(getClass().getSimpleName(),
//                                "Can't create file to take picture!");
//                    }
//                    mImageUri = Uri.fromFile(photo);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
//                    startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
// 
//                }
//            });
//         
//            builder.setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {
// 
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    galleryIntent.setType("image/*");
//                    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
//                    System.out.println("va a galeria");
//                }
//
//            });
//            _photoDialog = builder.create();
// 
//        }
//        return _photoDialog;
// 
//    }
//	
//	public void onClick(DialogInterface dialog, int which) {
//	    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//	    galleryIntent.setType("image/*");
//	    System.out.println("A VER si galeria");
//	    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
//	    System.out.println("GALERIA!");
//	}
//	
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//	    super.onSaveInstanceState(outState);
//	    if (mImageUri != null)
//	        outState.putString("Uri", mImageUri.toString());
//	}
//	 
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//	    super.onRestoreInstanceState(savedInstanceState);
//	    if (savedInstanceState.containsKey("Uri")) {
//	        mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
//	    }
//	}
//	
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    super.onActivityResult(requestCode, resultCode, data);
//	    if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
//	        mImageUri = data.getData();
//	        getImage(mImageUri);
//	    } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
//	            && resultCode == RESULT_OK) {
//	        getImage(mImageUri);
//	    }
//	}
//	
//	public void getImage(Uri uri) {
//		PhotoUtils photoUtils = new PhotoUtils(this);
//        Bitmap bounds = photoUtils.getImage(uri);
//        if (bounds != null) {
////            setImageBitmap(bounds);
////        	byte[] byteArray = photoUtils.BitmapToByteArray(bounds);
//        	Intent intent = new Intent(new CreateThreadActivity(), CreateThreadActivity.class);
////				intent.putExtra("bounds", byteArray);
//        	intent.putExtra("bounds", bounds);
//				System.out.println("intent");
//				 startActivity(intent);
//        	
////            setImage(bounds);
//        } else {
//        	Toast.makeText(DroidActivity.this, "FALLO EN getImage(Uri uri)",Toast.LENGTH_LONG).show();
//        	System.out.println("POR AKI NMO PASA");
//        }
//    }
//	
////	public void getImage(Uri uri) {
////	    Bitmap bounds = photoUtils.getImage(uri, new PhotoSetter(){
////	        @Override
////	        public void onPhotoDownloaded(Bitmap bitmap){
////	            setImageBitmap(bitmap);
////	        }
////	    });
////	}
//	
//	private void setImageBitmap(Bitmap bitmap){
//	    photoViewer.setImageBitmap(bitmap);
//	}
//	
}
