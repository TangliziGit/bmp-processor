package org.tanglizi.bmp.image

import java.io.DataInputStream
import java.nio.ByteBuffer

object BmpReader {
  def fromStream(stream: DataInputStream): BmpImage = {
    val bmpHeader: BmpHeader = BmpHeader.fromStream(stream)
    val dibHeader: DibHeader = DibHeader.fromStream(stream)
    val colorTable: Seq[Seq[(Short, Short, Short)]] = readColorTableFromStream(dibHeader.width, dibHeader.height, stream)

    new BmpImage(bmpHeader, dibHeader, colorTable)
  }

  def fromBuffer(buffer: ByteBuffer): BmpImage = {
    val bmpHeader: BmpHeader = BmpHeader.fromBuffer(buffer)
    val dibHeader: DibHeader = DibHeader.fromBuffer(buffer)
    val colorTable: Seq[Seq[(Short, Short, Short)]] = readColorTableFromBuffer(dibHeader.width, dibHeader.height, buffer)

    new BmpImage(bmpHeader, dibHeader, colorTable)
  }

  def readColorTableFromBuffer(width: Long, height: Long, buffer: ByteBuffer): Seq[Seq[(Short, Short, Short)]] = {
    for (y <- 0L until height) yield
      for (x <- 0L until width) yield (
          BmpUtil.unsigned(buffer.get()),
          BmpUtil.unsigned(buffer.get()),
          BmpUtil.unsigned(buffer.get())
        )
  }

  def readColorTableFromStream(width: Long, height: Long, stream: DataInputStream): Seq[Seq[(Short, Short, Short)]] =
    for (y <- 0L until height) yield
      for (x <- 0L until width) yield (
          BmpUtil.unsigned(stream.readByte()),
          BmpUtil.unsigned(stream.readByte()),
          BmpUtil.unsigned(stream.readByte())
        )
}
