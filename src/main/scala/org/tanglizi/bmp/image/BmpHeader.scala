package org.tanglizi.bmp.image

import java.io.{DataInputStream}
import java.nio.ByteBuffer

class BmpHeader(val id: Int = 0x4D42,
                val fileSize: Long,
                val colorTableOffset: Long = 0x36
               ) {
  def addContentToStream(buffer: ByteBuffer): ByteBuffer = {
    buffer.putShort(id.toShort)
    buffer.putInt(fileSize.toInt)
    buffer.putShort(0)
    buffer.putShort(0)
    buffer.putInt(colorTableOffset.toInt)
    buffer
  }

  override def toString: String = {
    s"""id $id
      |fileSize $fileSize
      |colorTableOffset $colorTableOffset
      |""".stripMargin
  }
}

object BmpHeader {
  def fromStream(stream: DataInputStream): BmpHeader = {
    val id = BmpUtil.unsigned(stream.readShort())
    val fileSize = BmpUtil.unsigned(stream.readInt())
    stream.readShort()
    stream.readShort()
    val offset = BmpUtil.unsigned(stream.readInt())

    new BmpHeader(id, fileSize, offset)
  }

  def fromBuffer(buffer: ByteBuffer): BmpHeader = {
    val id = BmpUtil.unsigned(buffer.getShort())
    val fileSize = BmpUtil.unsigned(buffer.getInt())
    buffer.getShort()
    buffer.getShort()
    val offset = BmpUtil.unsigned(buffer.getInt())

    new BmpHeader(id, fileSize, offset)
  }

  def of(dibHeader: DibHeader, colorTable: Seq[Seq[(Short, Short, Short)]]): BmpHeader = {
    val colorTableSize = dibHeader.width * dibHeader.height * 3
    new BmpHeader(fileSize = 14 + dibHeader.headerSize + colorTableSize)
  }
}