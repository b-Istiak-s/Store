package com.example.projecy;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.projecy.employee.EmployeeActivity;
import com.example.projecy.expenses.ExpenseActivity;
import com.example.projecy.model.FileProvider;
import com.example.projecy.model.PathUtil;
import com.example.projecy.model.SqliteEmployee;
import com.example.projecy.profit.ProfitActivity;
import com.example.projecy.sales.SalesActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    Button btnEmployees, btnIncome, btnExpenses, btnMonthlyProfit;
    Button btnExport, btnImport,btnShare;
    SqliteEmployee sqliteEmployee;

    File folder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_home);

        requestPermissions();
        btnEmployees = findViewById(R.id.btnEmployee);
        btnIncome = findViewById(R.id.btnSales);
        btnExpenses = findViewById(R.id.btnExpense);
        btnExport = findViewById(R.id.btnExport);
        btnImport = findViewById(R.id.btnImport);
        btnShare = findViewById(R.id.btnShare);
        btnMonthlyProfit = findViewById(R.id.btnMonthlyProfit);
        sqliteEmployee = new SqliteEmployee(HomeActivity.this);

        btnEmployees.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, EmployeeActivity.class);
            startActivity(intent);
            Animatoo.INSTANCE
                    .animateCard(HomeActivity.this);
        });

        btnIncome.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, SalesActivity.class);
            startActivity(intent);
        });

        btnExpenses.setOnClickListener(v->{
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(this);
            }
            Intent intent = new Intent(HomeActivity.this, ExpenseActivity.class);
            startActivity(intent, options.toBundle());

        });
        btnMonthlyProfit.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, ProfitActivity.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateDiagonal(HomeActivity.this);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()){
                //It is said that all apps can't access all the files by default for security. So it is necessary to grant this kind of permission otherwise the app can't work properly.
                //No permission manager can call it. So user must grant the permission manually. (It started happening above API level 29)
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", HomeActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(HomeActivity.this, "You can't access all the features even the app might crash unless you grant the permission.", Toast.LENGTH_LONG).show();
            }
        }

        btnExport.setOnClickListener(v-> performBackup());
        btnShare.setOnClickListener(v-> share());
        btnImport.setOnClickListener(v-> performRestore());
    }


    public void performBackup() {

        File folder = new File(String.valueOf(this.getExternalFilesDir(File.separator + this.getResources().getString(R.string.app_name))));

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();

        if (success) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Backup");
            builder.setMessage("Do you want to backup now?");
            builder.setPositiveButton("Save", (dialog, which) -> {
                String out = folder  + ".db";
                sqliteEmployee.backup(this, out);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        } else
            Toast.makeText(this, "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
    }

    public void performRestore() {

        AlertDialog.Builder choosingOption = new AlertDialog.Builder(this);
        choosingOption.setTitle("Do you want to manually choose location?");
        choosingOption.setPositiveButton(
                "No",
                (dialog,which) ->{
                    folder = new File("/storage/emulated/0/Android/data/com.example.projecy/files/");
                    restore();
                });
        choosingOption.setNegativeButton(
                "Yes",
                (dialog,which)->{
                    Intent requestFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    requestFileIntent.setType("*/*");
                    startActivityForResult.launch(requestFileIntent);
                }
        );
        choosingOption.show();

    }

    ActivityResultLauncher<Intent> startActivityForResult = this.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Uri contentUri = data.getData();
            String path = PathUtil.getPath(contentUri,this);
            try {
                sqliteEmployee.importDB(this, path);
            }catch (Exception e){
                Log.e("exception",e.toString());
            }
        }
    });

    public void share(){
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/octet-stream");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        share.putExtra(Intent.EXTRA_STREAM, new FileProvider().getDatabaseURI(this));
//        Uri.fromFile(new File(this.getDatabasePath("database.db").getAbsolutePath()));
        startActivity(Intent.createChooser(share, "Share database"));
    }

    public void restore(){
        if (folder.exists()) {
            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton(
                    "cancel",
                    (dialog1, which1) -> dialog1.dismiss());
            builderSingle.setAdapter(
                    arrayAdapter,
                    (dialog1, which1) -> {
                        try {
                            sqliteEmployee.importDB(this, files[which1].getPath());
                        } catch (Exception e) {
                            Toast.makeText(this, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(this, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }

    private void requestPermissions() {
        // below line is use to request permission in the current activity.
        // this method is use to handle error in runtime permissions
        Dexter.withContext(this)
                // below line is use to request the number of permissions which are required in our app.
                .withPermissions(Manifest.permission.CALL_PHONE,
                        // below is the list of permissions
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                // after adding permissions we are calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
                            Log.d("permissions_granted","all permissions are granted");
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, we will show user a dialog message.
                            Toast.makeText(HomeActivity.this, "You must grant all the permissions", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    // we are displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                // below line is use to run the permissions on same thread and to check the permissions
                .onSameThread().check();
    }
}