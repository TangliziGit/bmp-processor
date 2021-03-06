package org.tanglizi.bmp.image

import java.nio.{ByteBuffer, ByteOrder}

object BmpWriter {
  def toBytes(bmpImage: BmpImage): Seq[Byte] = {
    val buffer: ByteBuffer = ByteBuffer.allocate(bmpImage.bmpHeader.fileSize.toInt)
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    bmpImage.bmpHeader.addContentToStream(buffer)
    bmpImage.dibHeader.addContentToStream(buffer)
    bmpImage.colorTable.foreach(xs => {
      xs.foreach(rgb => {
        buffer.put(rgb._1.toByte)
        buffer.put(rgb._2.toByte)
        buffer.put(rgb._3.toByte)
      })
    })

    buffer.array()
  }
}
