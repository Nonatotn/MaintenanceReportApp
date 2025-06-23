package com.nonato.maintenancereportapp.ui.reports

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nonato.maintenancereportapp.utils.PdfUtil
import com.nonato.maintenancereportapp.data.AppDatabase
import com.nonato.maintenancereportapp.data.model.Report
import com.nonato.maintenancereportapp.data.model.ReportItem
import com.nonato.maintenancereportapp.viewmodel.ReportDetailViewModel
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.File

class ReportDetailFragment : Fragment() {
    private lateinit var binding: FragmentReportDetailBinding
    private lateinit var viewModel: ReportDetailViewModel
    private var reportId: Long = 0
    private val args: ReportDetailFragmentArgs by navArgs()

    // Adaptação para múltiplos tipos de item (texto/imagem)
    private lateinit var adapter: ReportItemAdapter

    // Registradores de resultado para câmera e galeria
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var tempImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera argumento reportId (Safe Args)
        reportId = arguments?.getLong("reportId") ?: 0L

        // Inicializa ViewModel com reportId (usando SavedStateHandle ou Factory)
        viewModel = ViewModelProvider(this).get(ReportDetailViewModel::class.java)

        // Inicia launchers para obter fotos
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && tempImageUri != null) {
                promptForImageCaption(tempImageUri!!)
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                promptForImageCaption(uri)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Configure RecyclerView de itens
        adapter = ReportItemAdapter()
        binding.recyclerItems.layoutManager = LinearLayoutManager(context)
        binding.recyclerItems.adapter = adapter

        // Observa itens do relatório
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        // Botão Adicionar Texto
        binding.btnAddTextItem.setOnClickListener {
            promptForTextItem()
        }

        // Botão Adicionar Imagem
        binding.btnAddImageItem.setOnClickListener {
            // Abre diálogo para escolher entre câmera ou galeria
            AlertDialog.Builder(requireContext())
                .setTitle("Adicionar Imagem")
                .setItems(arrayOf("Câmera", "Galeria")) { _, which ->
                    if (which == 0) {
                        // Captura foto via CameraX (ou Intent)
                        val values = ContentValues().apply {
                            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
                        }
                        tempImageUri = requireContext().contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                        tempImageUri?.let { cameraLauncher.launch(it) }
                    } else {
                        // Seleciona da galeria
                        galleryLauncher.launch("image/*")
                    }
                }
                .show()
        }

        // Botão Salvar/Gerar PDF
        binding.btnGeneratePdf.setOnClickListener {
            saveReportAndGeneratePdf()
        }
    }

    private fun promptForTextItem() {
        val input = EditText(context)
        input.hint = "Descrição"
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Novo Item de Texto")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val text = input.text.toString()
                if (text.isNotBlank()) {
                    val item = ReportItem(
                        reportId = reportId,
                        itemType = 0,
                        text = text,
                        imageUri = null,
                        caption = null
                    )
                    viewModel.addItem(item)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun promptForImageCaption(imageUri: Uri) {
        val input = EditText(context)
        input.hint = "Legenda da imagem"
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Legenda da Imagem")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val caption = input.text.toString()
                if (caption.isNotBlank()) {
                    val item = ReportItem(
                        reportId = reportId,
                        itemType = 1,
                        text = null,
                        imageUri = imageUri.toString(),
                        caption = caption
                    )
                    viewModel.addItem(item)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun saveReportAndGeneratePdf() {
        // Cria ou atualiza relatório
        val title = binding.inputReportTitle.text.toString()
        val client = binding.inputReportClient.text.toString()
        val responsible = binding.inputReportResponsible.text.toString()
        if (title.isBlank() || client.isBlank() || responsible.isBlank()) {
            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }
        val report = Report(id = reportId, title = title, client = client, responsible = responsible)
        // Se for novo (id=0), inserimos para obter ID
        viewModel.viewModelScope.launch {
            if (reportId == 0L) {
                val newId = AppDatabase.getInstance(requireContext()).reportDao().insert(report)
                reportId = newId
            } else {
                AppDatabase.getInstance(requireContext()).reportDao().update(report)
            }
            // Após salvar, gera PDF
            val items = viewModel.items.value ?: emptyList()
            val pdfFile = PdfUtil.createPdf(requireContext(), report, items)
            sharePdf(pdfFile)
        }
    }

    private fun sharePdf(pdfFile: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().packageName + ".provider",
            pdfFile
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Compartilhar relatório"))
    }
}
