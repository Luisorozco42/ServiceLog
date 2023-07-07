package com.example.servivelog.ui.gestiondiagnostico.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.text.Layout.Alignment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.user.ActiveUser
import com.example.servivelog.ui.gestiondiagnostico.view.FragmentDiagnosticoDirections
import com.example.servivelog.ui.gestiondiagnostico.viewmodel.GestioDiagnosisViewModel
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiagnosisAdapter(
    private val activity: Activity,
    var context: Context,
    var listD: List<DiagnosisItem>,
    var view: View,
    var gestioDiagnosisViewModel: GestioDiagnosisViewModel,
    val listaC: List<ComputerItem>
) : RecyclerView.Adapter<DiagnosisAdapter.MyHolder>() {

    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1002

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var serviceTag: TextView
        var lab: TextView
        var fecha: TextView
        var edit: ImageView
        var delete: ImageView
        var logo: ImageView

        init {
            serviceTag = itemView.findViewById(R.id.txtServiceTag)
            lab = itemView.findViewById(R.id.txtLab)
            fecha = itemView.findViewById(R.id.txtFecha)
            edit = itemView.findViewById(R.id.ivEdit)
            delete = itemView.findViewById(R.id.ivDelete)
            logo = itemView.findViewById(R.id.iconoCvComputadora)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.diagnosis_item, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listD.size
    }

    interface OnDeleteClickListener {
        fun onDeleteCliked(diagnosisItem: DiagnosisItem)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val diag = listD[position]
        holder.serviceTag.text = diag.ServiceTag
        holder.lab.text = diag.nombrelab
        holder.fecha.text = diag.fecha

        holder.edit.setOnClickListener {
            if (diag.ServiceTag == "Sin datos") {
                Toast.makeText(context, "La base de datos se encuentra vacía", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val action =
                    FragmentDiagnosticoDirections.actionFragmentDiagnosticoToFragmentEditarDiagnostico(
                        diag
                    )
                Navigation.findNavController(view).navigate(action)
            }
        }

        holder.delete.setOnClickListener {
            if (diag.ServiceTag == "Sin datos") {
                Toast.makeText(context, "La base de datos se encuentra vacía", Toast.LENGTH_SHORT)
                    .show()
            } else {
                gestioDiagnosisViewModel.deleteDiagnosis(diag)
                onDeleteClickListener?.onDeleteCliked(diag)
            }
        }

        holder.logo.setOnClickListener {
            if (diag.ServiceTag == "Sin datos") {
                Toast.makeText(context, "La base de datos se encuentra vacía", Toast.LENGTH_SHORT)
                    .show()
            } else {
                generarReporteDiagnosi(diag)
            }
        }
    }

    private fun generarReporteDiagnosi(diag: DiagnosisItem) {
        // Lógica para generar el reporte de diagnóstico

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Si dan que no se piden otra vez
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            val currentTime = Date()
            val timeFormat = SimpleDateFormat("HH_mm_ss", Locale.getDefault())
            val formattedTime = timeFormat.format(currentTime)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val carpeta = "/diagnosticoPDF"
            val path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + carpeta
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
                Toast.makeText(context, "Se ha creado el directorio", Toast.LENGTH_SHORT).show()
            }
            val file =
                File(
                    dir,
                    "${diag.ServiceTag}_${diag.nombrelab}_${currentDate}_${formattedTime}.pdf"
                )
            val uri =
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val fos = FileOutputStream(file)

            val computadora = listaC.find { it.ubicacion == diag.nombrelab }

            val document = Document() // Crear un objeto Document con tamaño de página A4
            document.setMargins(20f, 20f, 100f, 50f) // Establecer los márgenes izquierdo, derecho, superior e inferior en unidades de medida

            val pdfWriter = PdfWriter.getInstance(document, fos)

            val resourceId = R.raw.uca_logo_fondo_blanco
            val inputStream = context.resources.openRawResource(resourceId)
            val bufferedInputStream = BufferedInputStream(inputStream)
            val byteArray = bufferedInputStream.readBytes()

            val pageEvent = object : PdfPageEventHelper() {
                private lateinit var headerTemplate: PdfTemplate
                private var headerHeight: Float = 100f // Ajusta la altura deseada para el encabezado

                override fun onOpenDocument(writer: PdfWriter, document: Document) {
                    headerTemplate = writer.directContent.createTemplate(document.pageSize.width, headerHeight)
                }

                override fun onEndPage(writer: PdfWriter, document: Document) {
                    val cb = writer.directContent

                    val currentTime = Date()
                    val timeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                    val formattedTime = timeFormat.format(currentTime)

                    val footerTable = PdfPTable(2)
                    footerTable.widthPercentage = 100f
                    footerTable.totalWidth = document.pageSize.width - document.leftMargin() - document.rightMargin()

                    val leftCell = PdfPCell()
                    leftCell.addElement(Paragraph("PBX: (505) 2278-3923 al 27 ext. 1109"))
                    leftCell.addElement(Paragraph("direccion.administrativa@uca.edu.ni"))
                    leftCell.border = Rectangle.NO_BORDER
                    leftCell.fixedHeight = 40f
                    footerTable.addCell(leftCell)

                    val rightCell = PdfPCell(Paragraph(formattedTime))
                    rightCell.border = Rectangle.NO_BORDER
                    rightCell.horizontalAlignment = Element.ALIGN_RIGHT
                    footerTable.addCell(rightCell)

                    footerTable.writeSelectedRows(
                        0, 2,
                        document.leftMargin(),
                        document.bottomMargin() + 8,
                        cb
                    )

                    // Dibujar la imagen del encabezado en el PdfTemplate
                    val headerImage = Image.getInstance(byteArray)
                    headerImage.scaleToFit(100f, 100f)
                    headerTemplate.addImage(headerImage, 100f, 0f, 0f, headerHeight, 0f, 0f)

                    // Agregar el PdfTemplate al contenido directo de la página en la posición deseada
                    cb.addTemplate(headerTemplate, 20f, document.top())
                }
            }

            // Establecer el evento de página que combina el encabezado y el pie de página
            pdfWriter.pageEvent = pageEvent

            // Abrir el documento para escribir
            document.open()

            val titulo = Paragraph("REPORTE TECNICO")
            titulo.alignment = Element.ALIGN_CENTER
            titulo.font.size = 18f
            titulo.spacingBefore = 10f
            titulo.font.setStyle("bold")
            document.add(titulo)

            document.add(Paragraph(" "))

            var tabla = PdfPTable(2)
            tabla.widthPercentage = 100f
            tabla.setWidths(floatArrayOf(45f, 75f))
            tabla.horizontalAlignment = Element.ALIGN_LEFT

            var celda = PdfPCell(Phrase("Usuario: "))
            celda.border = Rectangle.NO_BORDER
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda)

            var celda2 = PdfPCell(Phrase(ActiveUser.userName))
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda2)

            document.add(tabla)

            document.add(Paragraph(" "))

            tabla = PdfPTable(2)
            tabla.widthPercentage = 100f
            tabla.setWidths(floatArrayOf(45f, 75f))
            tabla.horizontalAlignment = Element.ALIGN_LEFT

            celda = PdfPCell(Phrase("Laboratorio: "))
            celda.border = Rectangle.NO_BORDER
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda)

            celda2 = PdfPCell(Phrase(diag.nombrelab))
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda2)

            document.add(tabla)

            document.add(Paragraph(" "))

            tabla = PdfPTable(2)
            tabla.widthPercentage = 100f
            tabla.setWidths(floatArrayOf(45f, 75f))
            tabla.horizontalAlignment = Element.ALIGN_LEFT

            celda = PdfPCell(Phrase("Códigos de la computadora: "))
            celda.border = Rectangle.NO_BORDER
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda)

            celda2 = PdfPCell(Phrase("${diag.ServiceTag} / ${computadora?.noInventario}"))
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda2)

            document.add(tabla)

            document.add(Paragraph(" "))
            val descripcion = Paragraph("Descripción: ")
            document.add(descripcion)

            document.add(Paragraph(" "))

            tabla = PdfPTable(1)
            tabla.horizontalAlignment = Element.ALIGN_LEFT

            celda2 = PdfPCell(Phrase(diag.descripcion))
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda2)

            document.add(tabla)

            val documentacion = Paragraph("Documentacion:")
            document.add(documentacion)

            document.add(Paragraph(" "))

            val tableImages = PdfPTable(4) // Cambia el número de columnas según la cantidad de imágenes que deseas mostrar en una fila

            var imagen: Image

            if (diag.ruta1 != "") {
                imagen = Image.getInstance(diag.ruta1)
                imagen.scaleAbsolute(70f, 70f)
                val cell1 = PdfPCell(imagen)
                cell1.border = PdfPCell.NO_BORDER
                tableImages.addCell(cell1)
            }

            if (diag.ruta2 != "") {
                imagen = Image.getInstance(diag.ruta2)
                imagen.scaleAbsolute(70f, 70f)
                val cell2 = PdfPCell(imagen)
                cell2.border = PdfPCell.NO_BORDER
                tableImages.addCell(cell2)
            }

            if (diag.ruta3 != "") {
                imagen = Image.getInstance(diag.ruta3)
                imagen.scaleAbsolute(70f, 70f)
                val cell3 = PdfPCell(imagen)
                cell3.border = PdfPCell.NO_BORDER
                tableImages.addCell(cell3)
            }

            if (diag.ruta4 != "") {
                imagen = Image.getInstance(diag.ruta4)
                imagen.scaleAbsolute(70f, 70f)
                val cell4 = PdfPCell(imagen)
                cell4.border = PdfPCell.NO_BORDER
                tableImages.addCell(cell4)
            }

            document.add(tableImages)


            val cb = pdfWriter.directContent
            cb.setLineWidth(1f)
            cb.moveTo(100f, 300f) // Coordenadas de inicio de la línea
            cb.lineTo(200f, 300f) // Coordenadas de fin de la línea
            cb.stroke()

            // Segunda línea
            cb.moveTo(400f, 300f) // Coordenadas de inicio de la segunda línea
            cb.lineTo(500f, 300f) // Coordenadas de fin de la segunda línea
            cb.stroke()

            document.close()

            val pdfViewIntent = Intent(Intent.ACTION_VIEW)
            pdfViewIntent.setDataAndType(uri, "application/pdf")
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                context.startActivity(pdfViewIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "No hay aplicación para ver archivos PDF instalada",
                    Toast.LENGTH_SHORT
                ).show()
            }

            Toast.makeText(context, "Reporte generado correctamente", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun updateRecycler(listD: List<DiagnosisItem>) {
        this.listD = listD
        notifyDataSetChanged()
    }
}