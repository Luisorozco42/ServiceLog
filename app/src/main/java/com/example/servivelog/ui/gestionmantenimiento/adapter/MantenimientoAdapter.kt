package com.example.servivelog.ui.gestionmantenimiento.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.user.ActiveUser
import com.example.servivelog.ui.gestionmantenimiento.view.FragmentGestionMantenimientoDirections
import com.example.servivelog.ui.gestionmantenimiento.viewmodel.GestionManteViewModel
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Intent
import androidx.core.content.FileProvider
import com.itextpdf.text.Image
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfTemplate
import java.io.BufferedInputStream

class MantenimientoAdapter(
    private val activity: Activity,
    var context: Context,
    var listM: List<MantenimientoCUDItem>,
    var view: View,
    var gestionManteViewModel: GestionManteViewModel,
    val listaC: List<ComputerItem>
) : RecyclerView.Adapter<MantenimientoAdapter.MyHolder>() {

    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1001

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
            edit = itemView.findViewById(R.id.ivEdit)
            delete = itemView.findViewById(R.id.ivDelete)
            logo = itemView.findViewById(R.id.iconoCvComputadora)
            fecha = itemView.findViewById(R.id.txtFecha)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.maintenance_item, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listM.size
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked(mantenimientoCUDItem: MantenimientoCUDItem)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val mant = listM[position]
        holder.serviceTag.text = mant.computadora
        holder.lab.text = mant.labname
        holder.fecha.text = mant.dia

        holder.edit.setOnClickListener {
            if (mant.computadora == "Sin datos") {
                Toast.makeText(context, "La base de datos se encuentra vacia", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val action =
                    FragmentGestionMantenimientoDirections.actionFragmentGestionMantenimientoToFragmentEditMantenimiento(
                        mant
                    )
                Navigation.findNavController(view).navigate(action)
            }
        }

        holder.delete.setOnClickListener {
            if (mant.computadora == "Sin datos") {
                Toast.makeText(context, "La base de datos se encuentra vacia", Toast.LENGTH_SHORT)
                    .show()
            } else {
                gestionManteViewModel.deleteMantenimiento(mant)
                onDeleteClickListener?.onDeleteClicked(mant)
            }
        }

        holder.logo.setOnClickListener {

            if (mant.computadora == "Sin datos") {
                Toast.makeText(context, "La base de datos se encuentra vacia", Toast.LENGTH_SHORT)
                    .show()
            } else {
                generarReporteMantenimiento(mant)
            }
        }
    }

    fun generarReporteMantenimiento(mant: MantenimientoCUDItem) {
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

            val carpeta = "/mantenimientoPDF"
            val path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + carpeta
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
                Toast.makeText(context, "Se ha creado el directorio", Toast.LENGTH_SHORT).show()
            }
            val file =
                File(dir, "${mant.computadora}_${mant.labname}_${currentDate}_${formattedTime}.pdf")
            val uri =
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val fos = FileOutputStream(file)

            val computadora = listaC.find { it.serviceTag == mant.computadora }

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

            val titulo = Paragraph("REPORTE DE MANTENIMIENTO")
            titulo.alignment = Element.ALIGN_CENTER
            titulo.font.size = 18f
            titulo.spacingAfter = 10f
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

            celda2 = PdfPCell(Phrase(mant.labname))
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

            celda2 = PdfPCell(Phrase("${mant.computadora} / ${computadora?.noInventario}"))
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda2)

            document.add(tabla)

            document.add(Paragraph(" "))

            tabla = PdfPTable(2)
            tabla.widthPercentage = 100f
            tabla.setWidths(floatArrayOf(45f, 75f))
            tabla.horizontalAlignment = Element.ALIGN_LEFT

            celda = PdfPCell(Phrase("Tipo de mantenimiento: "))
            celda.border = Rectangle.NO_BORDER
            celda.horizontalAlignment = Element.ALIGN_LEFT
            celda.verticalAlignment = Element.ALIGN_CENTER
            tabla.addCell(celda)

            celda2 = PdfPCell(Phrase(mant.tipoLimpieza))
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda2)

            document.add(tabla)

            document.add(Paragraph(" "))
            val descripcion = Paragraph("Descripción: ")
            document.add(descripcion)

            document.add(Paragraph(" "))

            tabla = PdfPTable(1)
            tabla.horizontalAlignment = Element.ALIGN_LEFT

            celda2 = PdfPCell(Phrase(mant.desc))
            celda.horizontalAlignment = Element.ALIGN_LEFT
            tabla.addCell(celda2)

            document.add(tabla)

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

    fun updateRecycler(listM: List<MantenimientoCUDItem>) {
        this.listM = listM
        notifyDataSetChanged()
    }
}