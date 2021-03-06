package yo.digo.yodigo_wizard;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class BuscaImagenActivityCat extends Activity{
	
	
	ImageView iv_image,img1;
	int column_index;
	  Intent intent=getIntent();
	// Declare our Views, so we can access them later
	String logo,imagePath,Logo;
	Cursor cursor;
	//YOU CAN EDIT THIS TO WHATEVER YOU WANT
	private static final int SELECT_PICTURE = 1;

	 String selectedImagePath;
	//ADDED
	 String filemanagerstring;
	Activity act = this;

	public static final String IMAGEN_NAME_KEY = "IMAGEN_NAME_KEY";
	
	DatabaseHelper idh;//referencia a la BD
	Cursor imagenesResult;//resultado de query
	MyListSimpleCursorAdapter adaptador;//adaptador para el listview
	ListView listview;
	
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	    	Intent intent = getIntent();
	    	Cursor renglonContent = (Cursor) parent.getItemAtPosition(position);
	    	String nombreImagen = renglonContent.getString(1);
	    	intent.putExtra(AgregaActivity.IMAGEN_NAME_KEY, nombreImagen);
			act.setResult(RESULT_OK, intent);
			finish();
	    
	    }
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_image);
		
		//voy preparando la BD
		idh = new DatabaseHelper(this);
		idh.open();
		imagenesResult = idh.getImage();
		startManagingCursor(imagenesResult);
		//elemento3 es id de imagen, elemento4 es nombre de imagen
		String[] from = new String[] {DatabaseHelper.ELEMENTO_8,DatabaseHelper.ELEMENTO_8 };
		//referencio a cada renglon
		int[] to = new int[] { R.id.image_list_image, R.id.imageView1 };
		//formo mi adaptador
		adaptador = new MyListSimpleCursorAdapter(this,R.layout.listgriditem,imagenesResult,from,to);
		listview = (ListView) findViewById(R.id.listView1);
		listview.setAdapter(adaptador);
		listview.setOnItemClickListener(mMessageClickedHandler); 
		idh.close();
	}
	
	public void cancelar(View view) {
		this.setResult(RESULT_CANCELED);
		finish();
	}
	
	public void buscaSD(View view){
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	        if (requestCode == SELECT_PICTURE) {
	           Intent intent = getIntent();
	           Uri selectedImageUri = data.getData();

	            //OI FILE Manager
	           filemanagerstring = selectedImageUri.getPath();

	            //MEDIA GALLERY
	           selectedImagePath = getPath(selectedImageUri);
	           intent.putExtra(AgregaCategoriaTab.IMAGEN_NAME_KEY,imagePath);
	           act.setResult(3, intent);//2 es codigo para seleccion de imagen de la SD
	           finish();

	        }

	    }

	}

	//UPDATED!
	public String getPath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		column_index = cursor
		        .getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		 imagePath = cursor.getString(column_index);
	
		return cursor.getString(column_index);
	}
}
