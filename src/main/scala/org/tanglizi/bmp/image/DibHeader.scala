package org.tanglizi.bmp.image

import java.io.{DataInput, DataInputStream, DataOutputStream}
import java.nio.ByteBuffer

class DibHeader(val headerSize: Long = 0x28,
                val width: Long,
                val height: Long,
                val planes: Int = 0x01,
                val pixel: Int = 0x18,
                val compression: Long = 0,
                val rawDataSize: Long = 0,
                val xMetre: Long = 2835,
                val yMetre: Long = 2835,
                val colors: Long = 0,
                val importantColors: Long = 0,
                val useless: Array[Byte] = Array()
               ) {
  def addContentToStream(buffer: ByteBuffer): ByteBuffer = {
    buffer.putInt(headerSize.toInt)
    buffer.putInt(width.toInt)
    buffer.putInt(height.toInt)
    buffer.putShort(planes.toShort)
    buffer.putShort(pixel.toShort)
    buffer.putInt(compression.toInt)
    buffer.putInt(rawDataSize.toInt)
    buffer.putInt(xMetre.toInt)
    buffer.putInt(yMetre.toInt)
    buffer.putInt(colors.toInt)
    buffer.putInt(importantColors.toInt)
    buffer.put(useless)
    buffer
  }

  override def toString: String =
    s"""headerSize $headerSize
      |width $width
      |height $height
      |planes $planes
      |pixle $pixel
      |compression $compression
      |rawDataSize $rawDataSize
      |xMetre $xMetre
      |yMetre $yMetre
      |colors $colors
      |importantColors $importantColors
      |""".stripMargin
}

object DibHeader {
  def fromStream(stream: DataInputStream): DibHeader = new DibHeader(
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readShort()),
    BmpUtil.unsigned(stream.readShort()),
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readInt()),
    BmpUtil.unsigned(stream.readInt()),
  )

  def fromBuffer(buffer: ByteBuffer): DibHeader = {
    val headerSize = BmpUtil.unsigned(buffer.getInt())
    val useless = Array.ofDim[Byte](headerSize.toInt - 0x28)

    val header = new DibHeader(
      headerSize,
      BmpUtil.unsigned(buffer.getInt()),
      BmpUtil.unsigned(buffer.getInt()),
      BmpUtil.unsigned(buffer.getShort()),
      BmpUtil.unsigned(buffer.getShort()),
      BmpUtil.unsigned(buffer.getInt()),
      BmpUtil.unsigned(buffer.getInt()),
      BmpUtil.unsigned(buffer.getInt()),
      BmpUtil.unsigned(buffer.getInt()),
      BmpUtil.unsigned(buffer.getInt()),
      BmpUtil.unsigned(buffer.getInt()),
      useless
    )
    buffer.get(useless, 0, headerSize.toInt - 0x28)

    header
  }

  def useDefault(width: Long, height: Long): DibHeader =
    new DibHeader(width = width, height = height)

  def changeFrom(dibHeader: DibHeader, width: Long, height: Long): DibHeader =
    new DibHeader(
      dibHeader.headerSize,
      width, height,
      dibHeader.planes, dibHeader.pixel,
      dibHeader.compression, dibHeader.rawDataSize,
      dibHeader.xMetre, dibHeader.yMetre,
      dibHeader.colors, dibHeader.importantColors,
      dibHeader.useless.clone()
    )
}