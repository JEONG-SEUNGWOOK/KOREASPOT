package com.example.seungwook.koreaspot.Main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.seungwook.koreaspot.Globals;
import com.example.seungwook.koreaspot.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment extends Fragment {

    @Bind(R.id.name) TextView nameView;
    @Bind(R.id.sex_field)TextView sexView;
    @Bind(R.id.birth) TextView birthView;
    @Bind(R.id.height) TextView heightView;
    @Bind(R.id.weight) TextView weightView;
    private EditText heightInput;
    private EditText weightInput;
    private View positiveAction;
    public static final int RESULT_OK = -1;

    private String name, sex;
    private int uYear, uMonth, uDay;
    private String height, weight;

    @BindColor(R.color.medium_spring_green) int textColor;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private Uri mImageCaptureUri;
    private ImageView mProfilePicture;

    Bitmap roundPhoto;
    Globals globals;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProfileFragment(){

    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        mProfilePicture = (ImageView)rootView.findViewById(R.id.profile_picture);
        sharedPreferences = getActivity().getSharedPreferences("MyPreference", getActivity().MODE_WORLD_READABLE| getActivity().MODE_MULTI_PROCESS | getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        try {
            readSharedPreference();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initField();


        String image = sharedPreferences.getString("profileImage","");
        if(image.equals("")){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.basicprofile);
            mProfilePicture.setImageDrawable(bitmapDrawable);
        }
        else {
            roundPhoto = StringToBitMap(image);
            mProfilePicture.setImageBitmap(roundPhoto);
        }





        return rootView;
    }

    public void initField(){
        nameView.setText(name);
        sexView.setText(sex);
        birthView.setText(uYear +"." +uMonth +"." +uDay);
        heightView.setText(height+"cm");
        weightView.setText(weight+"kg");
    }


    @OnClick(R.id.nameContainer)
    public void showNameDialog() {
        new MaterialDialog.Builder(getActivity())
                .titleColorRes(R.color.medium_spring_green)
                .title("본인의 이름을 입력해주세요.")
                .content("2자 이상 입력해주세요.")
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRangeRes(2, 10, R.color.medium_spring_green)
                .negativeText(R.string.cancel)
                .negativeColorRes(R.color.medium_spring_green)
                .positiveText(R.string.update)
                .positiveColorRes(R.color.medium_spring_green)
                .input("이름", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        name = input.toString();
                        nameView.setText(name);
                        editor.putString("name", name).commit();
                        Toast.makeText(getActivity(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    @OnClick(R.id.sexContainer)
    public void showSexChoice() {
        new MaterialDialog.Builder(getActivity())
                .title("성별을 선택해주세요.")
                .titleColorRes(R.color.medium_spring_green)
                .items(R.array.sex_value)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        sexView.setText(text);
                        editor.putString("sex",text.toString()).commit();
                        Toast.makeText(getActivity(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
                        return true; // allow selection
                    }
                })
                .negativeText(R.string.cancel)
                .negativeColorRes(R.color.medium_spring_green)
                .positiveText(R.string.update)
                .positiveColorRes(R.color.medium_spring_green)
                .show();
    }

    @OnClick(R.id.heightContainer)
    public void showHeightDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("키를 입력해주세요.")
                .titleColorRes(R.color.medium_spring_green)
                .customView(R.layout.dialog_height, true)
                .negativeText(R.string.cancel)
                .negativeColorRes(R.color.medium_spring_green)
                .positiveText(R.string.update)
                .positiveColorRes(R.color.medium_spring_green)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        height = heightInput.getText().toString();
                        heightView.setText(height +"cm");
                        editor.putString("height",height).commit();
                        Toast.makeText(getActivity(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                }).build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        heightInput = (EditText) dialog.getCustomView().findViewById(R.id.editHeight);
        heightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 1);
                if(s.toString().length() > 0){
                    if(Double.parseDouble(s.toString()) > 300 ){
                        heightInput.setText("300.0");
                        Toast.makeText(getActivity(),"설정 가능 범위: 10~300cm",Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }
    @OnClick(R.id.weightContainer)
    public void showWeightDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("몸무게를 입력해주세요.")
                .titleColorRes(R.color.medium_spring_green)
                .customView(R.layout.dialog_weight, true)
                .negativeText(R.string.cancel)
                .negativeColorRes(R.color.medium_spring_green)
                .positiveText(R.string.update)
                .positiveColorRes(R.color.medium_spring_green)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        weight = weightInput.getText().toString();
                        weightView.setText(weight +"kg");
                        editor.putString("weight",weight).commit();
                        Toast.makeText(getActivity(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                }).build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        weightInput = (EditText) dialog.getCustomView().findViewById(R.id.editWeight);
        weightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 1);
                if(s.toString().length() > 0){
                    if(Double.parseDouble(s.toString()) > 500 ){
                        weightInput.setText("500.0");
                        Toast.makeText(getActivity(),"설정 가능 범위: 10~500kg",Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }

    @OnClick(R.id.birthContainer)
    public void showBirthDialog(){
        new DatePickerDialog(getActivity(), dateSetListener, uYear, uMonth, uDay).show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            uYear = year;
            uMonth = monthOfYear+1;
            uDay = dayOfMonth;
            birthView.setText(uYear +"." +uMonth +"." +uDay);
            editor.putInt("year",uYear)
                    .putInt("month",uMonth)
                    .putInt("day",uDay).commit();

            Toast.makeText(getActivity(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
        }
    };


    @OnClick(R.id.profile_picture)
    public void showProfilePictureSetting() {
        new MaterialDialog.Builder(getActivity())
                .title("업로드할 이미지 선택")
                .positiveText("사진촬영")
                .negativeText("앨범선택")
                .neutralText("삭제")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.basicprofile);
                        mProfilePicture.setImageDrawable(bitmapDrawable);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakeAlbumAction();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakePhotoAction();
                    }
                })
                .show();
    }


    /**
     * 카메라에서 이미지 가져오기
     */
    private void doTakePhotoAction()
    {
    /*
     * 참고 해볼곳
     * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
     * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
     * http://www.damonkohler.com/2009/02/android-recipes.html
     * http://www.firstclown.us/tag/android/
     */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
        //intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction()
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
        {
            return;
        }

        switch(requestCode)
        {
            case CROP_FROM_CAMERA:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if(extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    roundPhoto = globals.getRoundedBitmap(photo);
                    mProfilePicture.setImageBitmap(roundPhoto);

                    String image = BitMapToString(roundPhoto);
                    editor.putString("profileImage", image).commit();

                    Toast.makeText(getActivity(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();

                }
                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }

                break;
            }

            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }
    }


    public void readSharedPreference() throws Exception{
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreference", 0);
        name = sharedPreferences.getString("name","홍길동");
        sex = sharedPreferences.getString("sex","남자");
        uYear = sharedPreferences.getInt("year",1991);
        uMonth = sharedPreferences.getInt("month",12);
        uDay = sharedPreferences.getInt("day",25);
        height = sharedPreferences.getString("height","170");
        weight = sharedPreferences.getString("weight","60");

    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
