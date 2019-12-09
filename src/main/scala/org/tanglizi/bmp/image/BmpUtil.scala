package org.tanglizi.bmp.image

import java.io.{File, FileInputStream}
import java.nio.{ByteBuffer, ByteOrder}

object BmpUtil {
  def unsigned(x: Int): Long = 0xffffffffL & x
  def unsigned(x: Short): Int = 0xffff & x
  def unsigned(x: Byte): Short = (0xff & x).toShort

  def toBuffer(file: File): ByteBuffer = {
    val bytes = Array.ofDim[Byte](file.length().toInt)
    val stream = new FileInputStream(file)
    stream.read(bytes)

    val buffer = ByteBuffer.wrap(bytes)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer
  }
}
