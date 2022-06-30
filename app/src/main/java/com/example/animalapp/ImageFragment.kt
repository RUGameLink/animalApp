package com.example.animalapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Struct
import java.util.*


private lateinit var imageTitle: ImageView
private lateinit var saveButton: Button

class ImageFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        imageTitle = view.findViewById(R.id.imageTitle)
        saveButton = view.findViewById(R.id.saveButton)

        val url = requireArguments().getString("urlImage") //Получение отправленных данных с иной активити
        Picasso.get().load(url).into(imageTitle)

        saveButton.setOnClickListener(saveButtonListener)

        return view
    }

    private var saveButtonListener: View.OnClickListener = View.OnClickListener { //Слушатель кнопки
        ShareIamge() //Вызов метода
    }

    private fun ShareIamge() { //Метод, реализующий механизм шэринга картинки
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")
        val bitmap = imageTitle.drawable.toBitmap()
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "tempimage", null)
        val uri = Uri.parse(path)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)
    }
}
