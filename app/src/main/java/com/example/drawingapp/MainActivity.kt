package com.example.drawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.example.drawingapp.databinding.ActivityMainBinding
import com.example.drawingapp.databinding.DialogBrushSizeBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var brushDialogBinding: DialogBrushSizeBinding
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.ivBackground.setImageURI(it)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.drawingView.setBrushSize(10.toFloat())
        binding.ibBrush.setOnClickListener {
            showBrushSizeDialog()
        }
        binding.ibUndo.setOnClickListener {
            binding.drawingView.onUndoClick()
        }
        binding.ibRedo.setOnClickListener {
            binding.drawingView.onRedoClick()
        }

        binding.ibGallery.setOnClickListener {
            if(Build.VERSION.SDK_INT < 33){
                if(hasReadImagesPermission()){
                    contract.launch("image/*")
                }else{
                    requestPermission()
                }
            }else{
//                Toast.makeText(this, "have access", Toast.LENGTH_SHORT).show()
                contract.launch("image/*")
            }
        }

    }

    private fun requestPermission(){
        val permissionsToRequest = mutableListOf<String>()
        if(!hasReadImagesPermission()){
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }else{
            Toast.makeText(this, "have access", Toast.LENGTH_SHORT).show()
        }
        if(permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()){
            for(i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("permissions request: ", "${permissions[i]} was granted")
                }
            }
        }
    }
    private fun hasReadImagesPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun showBrushSizeDialog(){
        val brushDialog = Dialog(this)
        brushDialogBinding = DialogBrushSizeBinding.inflate(layoutInflater)
        brushDialog.setContentView(brushDialogBinding.root)
        brushDialogBinding.ibSmallBrush.setOnClickListener {
            binding.drawingView.setBrushSize(10.toFloat())
            brushDialog.dismiss()
        }
        brushDialogBinding.ibMediumBrush.setOnClickListener {
            binding.drawingView.setBrushSize(20.toFloat())
            brushDialog.dismiss()
        }
        brushDialogBinding.ibLargeBrush.setOnClickListener {
            binding.drawingView.setBrushSize(30.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }
    fun onPalletClick(view: View){
        val tempColor = (view as ImageButton).tag

        binding.drawingView.setColor(tempColor.toString())
    }

}