package com.example.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.drawingapp.databinding.ActivityMainBinding
import com.example.drawingapp.databinding.DialogBrushSizeBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var brushDialogBinding: DialogBrushSizeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.drawingView.setBrushSize(10.toFloat())
        binding.ibBrush.setOnClickListener {
            showBrushSizeDialog()
        }
    }

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

}