package com.example.libreria_optimizada

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var novelAdapter: NovelAdapter
    private val novels = mutableListOf<Novel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewNovels)
        novelAdapter = NovelAdapter(novels) { selectedNovel -> showNovelDetails(selectedNovel) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = novelAdapter

        // Botón para añadir novelas
        val fabAddNovel: FloatingActionButton = findViewById(R.id.fabAddNovel)
        fabAddNovel.setOnClickListener { showAddNovelDialog() }

        // Cargar novelas desde Firestore
        loadNovelsFromDatabase()
    }

    private fun loadNovelsFromDatabase() {
        db.collection("Novelas")
            .get()
            .addOnSuccessListener { result ->
                novels.clear()
                for (document in result) {
                    val novel = document.toObject(Novel::class.java)
                    novels.add(novel)
                }
                novelAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar las novelas: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAddNovelDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_novel, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.etTitle)
        val yearEditText: EditText = dialogView.findViewById(R.id.etYear)
        val authorEditText: EditText = dialogView.findViewById(R.id.etAuthor)

        AlertDialog.Builder(this)
            .setTitle("Add Novel")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleEditText.text.toString()
                val year = yearEditText.text.toString().toIntOrNull()
                val author = authorEditText.text.toString()

                if (title.isNotBlank() && year != null && author.isNotBlank()) {
                    val newNovel = Novel(title, year, author)
                    addNovelToDatabase(newNovel)
                } else {
                    Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addNovelToDatabase(novel: Novel) {
        db.collection("Novelas")
            .add(novel)
            .addOnSuccessListener {
                Toast.makeText(this, "Novel added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add novel.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showNovelDetails(novel: Novel) {
        AlertDialog.Builder(this)
            .setTitle(novel.title)
            .setMessage("Author: ${novel.author}\nYear: ${novel.year}")
            .setPositiveButton("OK", null)
            .show()
    }
}
