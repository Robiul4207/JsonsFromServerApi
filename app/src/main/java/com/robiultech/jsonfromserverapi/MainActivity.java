package com.robiultech.jsonfromserverapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    LottieAnimationView lottieAnimationView;
    public static ArrayList<HashMap<String,String>> arrayList= new ArrayList<>();
    HashMap<String,String> hashMap;
    int totalItem = 0,totalTK = 0;
    RelativeLayout layRelay;
    ImageView bag;
    TextView items,total;
    EditText editText;
    LinearLayout mainlay,cartLay;

    Button completeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layRelay=findViewById(R.id.relativeId);
        bag=findViewById(R.id.bag);
        items=findViewById(R.id.items);
        total=findViewById(R.id.total);
        editText=findViewById(R.id.cartId);
        mainlay=findViewById(R.id.mainLayout);
        cartLay=findViewById(R.id.cartLay);
        completeBtn=findViewById(R.id.submitBtn);
        // change actionbar/statusbar color
        //actionBar= getSupportActionBar();
        getSupportActionBar().hide();
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40442891")));
            // change notification bar color
        if(Build.VERSION.SDK_INT>=21){
            //window=this.getWindow();
            //window.setStatusBarColor(this.getResources().getColor(R.color.notificationBar));
            getWindow().setStatusBarColor(Color.parseColor("#0AC4C4"));

        }
        listView=findViewById(R.id.listView);
        lottieAnimationView=findViewById(R.id.myLottie);


        MyAdapter myAdapter= new MyAdapter();
        listView.setAdapter(myAdapter);

        String url ="https://dummyjson.com/products";
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                lottieAnimationView.setVisibility(View.GONE);
                Log.d("server request",response.toString());
                try {
                    JSONArray productArray = response.getJSONArray("products");
                    for(int i=0;i<productArray.length();i++) {
                        JSONObject jsonObject = productArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String price = jsonObject.getString("price");
                        String brand = jsonObject.getString("brand");
                        String category = jsonObject.getString("category");
                        String stock = jsonObject.getString("stock");
                        String rating = jsonObject.getString("rating");
                        String description = jsonObject.getString("description");
                        String thumbnail = jsonObject.getString("thumbnail");
                        // initialize
                        hashMap = new HashMap<>();
                        hashMap.put("title",title);
                        hashMap.put("price",price);
                        hashMap.put("brand",brand);
                        hashMap.put("category",category);
                        hashMap.put("stock",stock);
                        hashMap.put("rating",rating);
                        hashMap.put("description",description);
                        hashMap.put("thumbnail",thumbnail);
                        arrayList.add(hashMap);



                         JSONArray jsonArrayImage= jsonObject.getJSONArray("images");
                         for (int j=0;j<jsonArrayImage.length();j++){

                             String imageUrl= jsonArrayImage.getString(j);
                             //textView.append(imageUrl);
                             //textView.append("\n");
                         }


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lottieAnimationView.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
    class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MyAdapter(){
            this.inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.sample_listitem,parent,false);
            */
            convertView = inflater.inflate(R.layout.item_layout, parent, false);

            TextView tvTitle =convertView.findViewById(R.id.itemTitle);
            TextView tvPrice =convertView.findViewById(R.id.itemPrice);
            TextView tvBrand =convertView.findViewById(R.id.itemBrand);
            TextView tvCategory =convertView.findViewById(R.id.itemcategory);
            TextView tvStock =convertView.findViewById(R.id.itemStock);
           // RatingBar tvRating =convertView.findViewById(R.id.rating);
            TextView tvRating =convertView.findViewById(R.id.rating);
            TextView tvDescription =convertView.findViewById(R.id.itemDes);
            ImageView imaThumb= convertView.findViewById(R.id.imageView);
            LinearLayout layItem=convertView.findViewById(R.id.layItem);
            Button adtoCart = convertView.findViewById(R.id.adtocartId);

            Animation animation1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_grid);
            animation1.setStartOffset(position*200);
            Animation animation;
            animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.move);
            convertView.startAnimation(animation1);
            HashMap<String,String> hashMap = arrayList.get(position);
            String title = hashMap.get("title");
            String price = hashMap.get("price");
            String brand = hashMap.get("brand");
            String category = hashMap.get("category");
            String stock = hashMap.get("stock");
            String rating = hashMap.get("rating");
            String description = hashMap.get("description");
            String thumb_url = hashMap.get("thumbnail");
            // https://img.youtube.com/vi/<insert-youtube-video-id-here>/0.jpg
            //String thumb_url="https://img.youtube.com/vi/"+videoId+"/0.jpg";
            Picasso.get().load(thumb_url).placeholder(R.drawable.rafsan).into(imaThumb);
            tvTitle.setText(title);
            tvDescription.setText("Desc: "+description);
            tvPrice.setText("USD: "+price);
            tvBrand.setText("Brand: "+brand);
            tvCategory.setText("Category: "+category);
            tvStock.setText("Stock: "+stock);
            tvRating.setText("Rating: "+rating);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            tvTitle.setBackgroundColor(color);
            adtoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layRelay.setVisibility(View.VISIBLE);
                    totalItem=totalItem+1;
                    totalTK=totalTK+Integer.parseInt(price);
                    items.setText("Items: "+totalItem);
                    total.setText("Total: "+totalTK+" USD");
                    editText.append("\n");
                    editText.append(title+": "+price+" USD");
                    bag.startAnimation(animation);
                    bag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mainlay.setVisibility(View.GONE);
                            cartLay.setVisibility(View.VISIBLE);

                            editText.append("\n-----------------------------------\n Total: "+totalTK+" USD");


                        }
                    });
                    completeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mainlay.setVisibility(View.VISIBLE);
                            cartLay.setVisibility(View.GONE);

                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"bangoacademy@gmail.com","robiultec@gmail.com"});
                            email.putExtra(Intent.EXTRA_SUBJECT, "New Order");
                            email.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());

                        //need this to prompt`enter code here`s email client only
                           // email.setType("message/rfc822");
                            email.setType("text/plain");

                            startActivity(Intent.createChooser(email, "Choose an Email client :"));

                        }
                    });
                    /*
                    Intent intent= new Intent(getApplicationContext(),CartActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("des",description);
                    intent.putExtra("price",price);
                    Bitmap bitmap = ((BitmapDrawable)imaThumb.getDrawable()).getBitmap();
                    CartActivity.MY_BITMAP=bitmap;
                    startActivity(intent);

                     */


                }
            });
            return convertView;
        }

    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Confirmation");
        builder.setMessage("Dou you want to exit ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}