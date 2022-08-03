package Steganography;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Steganography {
        public static int count = 0;

        public Steganography() {

        }

        private BufferedImage user_space(BufferedImage image) {
                BufferedImage new_img = new BufferedImage(image.getWidth(), image.getHeight(),
                                BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D graphics = new_img.createGraphics();
                graphics.drawRenderedImage(image, null);
                graphics.dispose();
                return new_img;
        }

        private byte[] get_byte_data(BufferedImage image) {
                WritableRaster raster = image.getRaster();
                DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();

                return buffer.getData();
        }

        private byte[] decode_text(byte[] image) {
                int length = 0;
                int offset = 32;
                for (int i = 0; i < 32; ++i) // i=24 will also work, as only the 4th byte contains real data
                {
                        length = (length << 1) | (image[i] & 1);
                }

                byte[] result = new byte[length];

                for (int b = 0; b < result.length; ++b) {
                        for (int i = 0; i < 8; ++i, ++offset) {
                                result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
                        }
                }
                return result;
        }

        private byte[] encode_text(byte[] image, byte[] addition, int offset) {
                if (addition.length + offset > image.length) {
                        throw new IllegalArgumentException("File not long enough!");
                }
                // loop through each addition byte
                for (int i = 0; i < addition.length; ++i) {
                        // loop through the 8 bits of each byte
                        int add = addition[i];
                        for (int bit = 7; bit >= 0; --bit, ++offset)
                        {
                                int b = (add >>> bit) & 1;
                                image[offset] = (byte) ((image[offset] & 0xFE) | b);
                        }
                }
                return image;
        }

        private BufferedImage getImage(String f) {
                BufferedImage image = null;
                File file = new File(f);

                try {
                        image = ImageIO.read(file);
                } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Image could not be read!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
                return image;
        }

        private void setImage(BufferedImage image, File file, String ext) {
                try {
                        file.delete(); // delete resources used by the File
                        ImageIO.write(image, ext, file);
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                        "File could not be saved!", "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        private BufferedImage add_text(BufferedImage image, String text) {
                byte img[] = get_byte_data(image);
                byte msg[] = text.getBytes();
                byte len[] = bit_conversion(msg.length);
                try {
                        encode_text(img, len, 0); // 0 first positiong
                        encode_text(img, msg, 32); // 4 bytes of space for length: 4bytes*8bit = 32 bits
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
                return image;
        }

        private byte[] bit_conversion(int i) {
                // only using 4 bytes
                byte byte3 = (byte) ((i & 0xFF000000) >>> 24); // 0
                byte byte2 = (byte) ((i & 0x00FF0000) >>> 16); // 0
                byte byte1 = (byte) ((i & 0x0000FF00) >>> 8); // 0
                byte byte0 = (byte) ((i & 0x000000FF));
                byte[] n = new byte[] { byte3, byte2, byte1, byte0 };
                return n;
        }

        public String decode(String file_path) {
                byte[] decode;
                try {
                        BufferedImage image = user_space(getImage(file_path));
                        decode = decode_text(get_byte_data(image));
                        return (new String(decode));
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "There is no hidden message in this image!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return "";
                }
        }


        public String encode(String file_path, String stegan, String message) {
                String file_path1;
                String img_name;
                img_name = file_path.substring(file_path.lastIndexOf("\\") + 1);
                String storage_path = file_path.substring(0, file_path.lastIndexOf("\\"));
                String new_img = img_name.substring(0, img_name.lastIndexOf("."));
                new_img = new_img.concat("_cpy.");
                new_img = new_img.concat(img_name.substring(img_name.lastIndexOf(".") + 1));
                file_path1 = storage_path + "\\" + new_img;
                File trial = new File(file_path1);
                while (trial.exists()) {
                        count++;
                        String s1 = file_path1.substring(0, file_path1.lastIndexOf("."));
                        s1 = s1 + count + "." + file_path1.substring(file_path1.lastIndexOf(".") + 1);
                        file_path1 = s1;
                        trial = new File(file_path1);
                        if (trial.exists())
                                continue;
                        else
                                break;
                }
                BufferedImage image_orig = getImage(file_path);
                BufferedImage image = user_space(image_orig);
                image = add_text(image, message);
                File dest = new File(file_path1);
                setImage(image, dest, "png");
                return file_path1;
        }

}
