import java.awt.*;  
  
import javax.swing.*;  
import java.awt.event.*;  
import java.io.*;  
import javax.sound.sampled.*;  
  
public class MyRecord extends JFrame implements ActionListener{  
  
    //format 
    AudioFormat af = null;  

    //mic data
    TargetDataLine td = null;  

    //sounder data  
    SourceDataLine sd = null;  

    //IO Stream
    ByteArrayInputStream bais = null;  
    ByteArrayOutputStream baos = null;  

    //Audio Input 
    AudioInputStream ais = null;  

    //control stop flags 
    Boolean stopflag = false;  
      
      
    //component
    JPanel jp1;  
    JButton captureBtn,stopBtn,playBtn,saveBtn;  

    public MyRecord()  
    {  
        jp1 = new JPanel();  

        captureBtn = new JButton("開始錄音");  
        captureBtn.addActionListener(this);  
        captureBtn.setActionCommand("captureBtn");  

        stopBtn = new JButton("停止錄音");  
        stopBtn.addActionListener(this);  
        stopBtn.setActionCommand("stopBtn");  
  
        playBtn = new JButton("播放錄音");  
        playBtn.addActionListener(this);  
        playBtn.setActionCommand("playBtn");  
       
        saveBtn = new JButton("保存錄音");  
        saveBtn.addActionListener(this);  
        saveBtn.setActionCommand("saveBtn");  
          
          
        this.add(jp1,BorderLayout.NORTH);  
        jp1.setLayout(null);  
        jp1.setLayout(new GridLayout(1, 4,10,10));  
        jp1.add(captureBtn);  
        jp1.add(stopBtn);  
        jp1.add(playBtn);  
        jp1.add(saveBtn);  
         
        captureBtn.setEnabled(true);  
        stopBtn.setEnabled(false);  
        playBtn.setEnabled(false);  
        saveBtn.setEnabled(false);  
       
        this.setSize(400,100);  
        this.setTitle("Recorder");  
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);  
        this.setVisible(true);  
          
          
    }  
      
    public void actionPerformed(ActionEvent e) {  //按鈕狀態
          
        if(e.getActionCommand().equals("captureBtn"))  
        {  
           
            captureBtn.setEnabled(false);  
            stopBtn.setEnabled(true);  
            playBtn.setEnabled(false);  
            saveBtn.setEnabled(false);  
         
            capture();  
        }else if (e.getActionCommand().equals("stopBtn")) {  
       
            captureBtn.setEnabled(true);  
            stopBtn.setEnabled(false);  
            playBtn.setEnabled(true);  
            saveBtn.setEnabled(true);  
               
            stop();  
              
        }else if(e.getActionCommand().equals("playBtn"))  
        {  
  
            play();  
        }else if(e.getActionCommand().equals("saveBtn"))  
        {  

            save();  
        }  
          
    }  
    //錄音
    public void capture()
    {  
        try {  
            
            af = getAudioFormat();  
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,af);  
            td = (TargetDataLine)(AudioSystem.getLine(info));  
           
            td.open(af);  
   
            td.start();  
              
            //create a thread to record
            Record record = new Record();  
            Thread t1 = new Thread(record);  
            t1.start();  
              
        } catch (LineUnavailableException ex) {  
            ex.printStackTrace();  
            return;  
        }  
    }  
    //停止錄音  
    public void stop()  
    {  
        stopflag = true;              
    }  
    //播放錄音
    public void play()  
    {  
        //將資料轉為bytes
        byte audioData[] = baos.toByteArray();  
        //再將轉換過的bytes轉為inputStream（串流） 
        bais = new ByteArrayInputStream(audioData);  
        af = getAudioFormat();  
        ais = new AudioInputStream(bais, af, audioData.length/af.getFrameSize());  
          
        try {  
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);  
            sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);  
            sd.open(af);  
            sd.start();  
            //Play
            Play py = new Play();  
            Thread t2 = new Thread(py);  
            t2.start();             
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            try {  
                //關閉串流
                if(ais != null)  
                {  
                    ais.close();  
                }  
                if(bais != null)  
                {  
                    bais.close();  
                }  
                if(baos != null)  
                {  
                    baos.close();  
                }  
                  
            } catch (Exception e) {       
                e.printStackTrace();  
            }  
        }  
          
    }  
    //保存錄音 
    public void save()  
    {  
         //取得格式  
        af = getAudioFormat();  
  
        byte audioData[] = baos.toByteArray();  
        bais = new ByteArrayInputStream(audioData);  
        ais = new AudioInputStream(bais,af, audioData.length / af.getFrameSize());  
        //檔名
        File file = null;  
        //寫入 
        try {     
            
            File filePath = new File(System.getProperty("user.home") + "/Desktop");  //存在桌面
            if(!filePath.exists())  
            { 
                filePath.mkdir();  
            }  //以秒數命名檔案
            file = new File(filePath.getPath()+"/"+System.currentTimeMillis()+".mp3");        
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            //關閉串流 
            try {  
                  
                if(bais != null)  
                {  
                    bais.close();  
                }   
                if(ais != null)  
                {  
                    ais.close();          
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }         
        }  
    }  
    //設定格式參數
    public AudioFormat getAudioFormat()   
    {  
        AudioFormat.Encoding encoding = AudioFormat.Encoding.  
        PCM_SIGNED ;  
        float rate = 8000f;  
        int sampleSize = 16;  
        String signedString = "signed";  
        boolean bigEndian = true;  
        int channels = 1;  
        return new AudioFormat(encoding, rate, sampleSize, channels,  
                (sampleSize / 8) * channels, rate, bigEndian);  
    }  
    //為了方便作為內部類別
    class Record implements Runnable  
    {  
        //緩衝區 
        byte bts[] = new byte[10000];  
    
        public void run() {   
            baos = new ByteArrayOutputStream();       
            try {  
                stopflag = false;  
                while(stopflag != true)  
                {   
                    //cnt：實際bytes數
                    int cnt = td.read(bts, 0, bts.length);  
                    if(cnt > 0)  
                    {  
                        baos.write(bts, 0, cnt);  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }finally{  
                try {  
                    //關閉串流
                    if(baos != null)  
                    {  
                        baos.close();  
                    }     
                } catch (IOException e) {  
                    e.printStackTrace();  
                }finally{  
                    td.drain();  
                    td.close();  
                }  
            }  
        }  
          
    }  

    class Play implements Runnable  //實作thread
    {  
        //直接播放baos的內容  
        public void run() {  
            byte bts[] = new byte[10000];  
            try {  
                int cnt;  
                //讀到緩衝區 
                while ((cnt = ais.read(bts, 0, bts.length)) != -1)   
                {  
                    if (cnt > 0)   
                    {  
                        //讀到sounder
                        sd.write(bts, 0, cnt);  
                    }  
                }  
                 
            } catch (Exception e) {  
                e.printStackTrace();  
            }finally{  
                 sd.drain();  
                 sd.close();  
            }  
              
              
        }         
    }     
}