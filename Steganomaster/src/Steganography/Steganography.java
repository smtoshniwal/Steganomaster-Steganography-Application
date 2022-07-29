
package Steganography;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class Steganography {
    public static int count=0;
    public Steganography()
        {
           
        }
        public String encode(String file_path, String stegan, String message)
        {
            String file_path1;
            String img_name;
            //String new_img="1";
            img_name=file_path.substring(file_path.lastIndexOf("\\")+1);
            String storage_path=file_path.substring(0, file_path.lastIndexOf("\\"));
            //System.out.println("name="+img_name);
            //System.out.println("path="+storage_path);
            String new_img=img_name.substring(0,img_name.lastIndexOf("."));
            new_img=new_img.concat("_cpy.");
            new_img=new_img.concat(img_name.substring(img_name.lastIndexOf(".")+1));
            //System.out.println("new_img_name="+new_img);
            file_path1=storage_path+"\\"+new_img;
            File trial=new File(file_path1);
            while(trial.exists()){
                //System.out.println("file_path1 before="+file_path1);
                count++;
                String s1=file_path1.substring(0,file_path1.lastIndexOf("."));
                s1=s1+count+"."+file_path1.substring(file_path1.lastIndexOf(".")+1);
                file_path1=s1;
                trial=new File(file_path1);
                ///System.out.println("file_path1 after="+file_path1);
                if(trial.exists())continue;
                else break;
            }
            //System.out.println("file_path1="+file_path1);
                BufferedImage image_orig = getImage(file_path);
                BufferedImage image = user_space(image_orig);
                image = add_text(image,message);
                //System.out.println("file_actual path for copy="+file_path1);
                File dest=new File(file_path1);
                setImage(image,dest,"png");
                return file_path1;
        }
       
        public String decode(String file_path)
        {
                byte[] decode;
                try
                {
                        BufferedImage image  = user_space(getImage(file_path));
                        decode = decode_text(get_byte_data(image));
                        return(new String(decode));
                }
                catch(Exception e)
                {
                        JOptionPane.showMessageDialog(null, "There is no hidden message in this image!","Error",JOptionPane.ERROR_MESSAGE);
                        return "";
                }
        }
        private BufferedImage getImage(String f)
        {
                BufferedImage   image   = null;
                File            file    = new File(f);
               
                try
                {
                        image = ImageIO.read(file);
                }
                catch(Exception ex)
                {
                        JOptionPane.showMessageDialog(null, "Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
                }
                return image;
        }
       
        private void setImage(BufferedImage image, File file, String ext)
        {
                try
                {
                        file.delete(); //delete resources used by the File
                        ImageIO.write(image,ext,file);
                }
                catch(Exception e)
                {
                        JOptionPane.showMessageDialog(null,
                                "File could not be saved!","Error",JOptionPane.ERROR_MESSAGE);
                }
        }
        private BufferedImage add_text(BufferedImage image, String text)
        {
                //convert all items to byte arrays: image, message, message length
                byte img[]  = get_byte_data(image);
                /*System.out.println("image get bytes:");
              for(int i=0;i<img.length;i++)System.out.println(img[i]);*/              
                //System.out.println("len of img="+img.length);
               
                byte msg[] = text.getBytes();
                //for(int i=0;i<msg.length;i++)System.out.print(msg[i]);
               // System.out.println("msg len byte="+msg.length);
                byte len[]   = bit_conversion(msg.length);
                //System.out.println("len of len[]="+len.length);
                try
                {
                        encode_text(img, len,  0); //0 first positiong
                        encode_text(img, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
                }
                catch(Exception e)
                {
                        JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
                }
                return image;
        }
       
        private BufferedImage user_space(BufferedImage image)
        {
                //create new_img with the attributes of image
                BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D      graphics = new_img.createGraphics();
                graphics.drawRenderedImage(image, null);
                graphics.dispose(); //release all allocated memory for this image
                return new_img;
        }
       
        private byte[] get_byte_data(BufferedImage image)
        {
                WritableRaster raster   = image.getRaster();
                DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
               
                return buffer.getData();
        }
       

        private byte[] bit_conversion(int i)
        {            
                //only using 4 bytes
                byte byte3 = (byte)((i & 0xFF000000) >>> 24); //0
                byte byte2 = (byte)((i & 0x00FF0000) >>> 16); //0
                byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); //0
                byte byte0 = (byte)((i & 0x000000FF)       );
                //{0,0,0,byte0} is equivalent, since all shifts >=8 will be 0
              byte[] n=new byte[]{byte3,byte2,byte1,byte0};
            //for(int i1=0;i1<4;i1++)System.out.println("byte"+(3-i1)+"="+n[i1]);
                //return(new byte[]{byte3,byte2,byte1,byte0});
            return n;
        }
       

        private byte[] encode_text(byte[] image, byte[] addition, int offset)
        {
                //check that the data + offset will fit in the image
            //System.out.println("Method called");
                if(addition.length + offset > image.length)
                {
                        throw new IllegalArgumentException("File not long enough!");
                }
                //loop through each addition byte
                for(int i=0; i<addition.length; ++i)
                {
                        //loop through the 8 bits of each byte
                        int add = addition[i];
                        for(int bit=7; bit>=0; --bit, ++offset) //ensure the new offset value carries on through both loops
                        {
                                //assign an integer to b, shifted by bit spaces AND 1
                                //a single bit of the current byte
                                int b = (add >>> bit) & 1;
                                //assign the bit by taking: [(previous byte value) AND 0xfe] OR bit to add
                                //changes the last bit of the byte in the image to be the bit of addition
                                image[offset] = (byte)((image[offset] & 0xFE) | b );
                        }
                }
                return image;
        }
       
        private byte[] decode_text(byte[] image)
        {
                int length = 0;
                int offset  = 32;
                //loop through 32 bytes of data to determine text length
                for(int i=0; i<32; ++i) //i=24 will also work, as only the 4th byte contains real data
                {
                        length = (length << 1) | (image[i] & 1);
                }
               
                byte[] result = new byte[length];
               
                //loop through each byte of text
                for(int b=0; b<result.length; ++b )
                {
                        //loop through each bit within a byte of text
                        for(int i=0; i<8; ++i, ++offset)
                        {
                                //assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
                                result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
                        }
                }
                //for(int i=0;i<result.length;i++)
                    //System.out.println(result[i]);
               
                return result;
        }
   
}

