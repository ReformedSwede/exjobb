package main;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;

/**
 * Generates QR-code images from a String input (usually an IP-address)
 *
 * Uses the following libraries which is under Apache License version 2.0:
 *
 *  com.google.zxing.BarcodeFormat
 *  com.google.zxing.WriterException
 *  com.google.zxing.common.BitMatrix
 *  com.google.zxing.qrcode.QRCodeWriter
 *
 * See license in License.txt in the project root.
 *
 * @author Robin Punell
 * @author david
 * @version 2.0
 */
public class QRGen
{
    private static final int QR_SIZE = 150;
    private QRCodeWriter writer;

    /**
     * Constructs a QR-generator
     * Creates a QRCodeWriter
     */
    public QRGen()
    {
        writer = new QRCodeWriter();
    }

    /**
     * Generates a qr-code from a ip address
     * @param ip the ip-address to encode
     * @return a QR image
     */
    public BufferedImage getQR(String ip)
    {
        BitMatrix matrix = new BitMatrix(QR_SIZE,QR_SIZE);
        try
        {
            matrix = writer.encode(
                    ip, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);
        }catch (WriterException e)
        {
            e.printStackTrace();
        }

        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}