package hour;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hour extends javax.swing.JFrame {

    public Calendar c =null;
    //public String url = "jdbc:sqlite:C:\\sql_LITE\\DbHorario.db";
    public String url = "jdbc:sqlite:C:\\sql_LITE\\DbHora.db";
    public String minutos="",hora="",state="",dia="",mes="",anio="";
    public String sqlSelect="SELECT dia,mes,anio,horaEntrada,horaSalida FROM tabla_horas";
    
    public Hour() {
        initComponents();
        this.setTitle("horarios");
        setIconImage( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/oso.jpg")) );
        campo_Secreto.setVisible(false);
        setBounds(300, 100, 640, 460);
        mostrarVisualmenteFechaHora();
        campo_Area.setEditable(false);
        getContentPane().setBackground(new Color(48,16,145));
    }

    public void UpdateState(){
        c = new GregorianCalendar();
        if(c.get(Calendar.AM_PM) == 0){ 
            state = "am"; 
        }else{
            state = "pm";  
        }
    }
    
    public void UpdateMinutes(){
        c = new GregorianCalendar();
        if( c.get(Calendar.MINUTE) < 10 ){
            minutos="0"+Integer.toString( c.get(Calendar.MINUTE) );
        }else{
            minutos=Integer.toString( c.get(Calendar.MINUTE) );
        }
    }
    
    public void UpdateHour(){
       c = new GregorianCalendar();
       if(c.get(Calendar.HOUR_OF_DAY)==13){
          hora = Integer.toString(1);    
       }else if(c.get(Calendar.HOUR_OF_DAY)==14){
          hora = Integer.toString(2);    
       }else if(c.get(Calendar.HOUR_OF_DAY)==15){
          hora = Integer.toString(3);    
       }else if(c.get(Calendar.HOUR_OF_DAY)==16){
          hora = Integer.toString(4);    
       }else if(c.get(Calendar.HOUR_OF_DAY)==17){
          hora = Integer.toString(5);    
       }else if(c.get(Calendar.HOUR_OF_DAY)==18){
          hora = Integer.toString(6);    
       }else if(c.get(Calendar.HOUR_OF_DAY)==19){
          hora = Integer.toString(7);    
       }else if(c.get(Calendar.HOUR_OF_DAY)==20){
          hora = Integer.toString(8);    
       }else{
         hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));  
       }//else
    }
    
    public String UpdateHourFull(){
        UpdateState();
        UpdateMinutes();
        UpdateHour(); 
        return hora+":"+minutos+" "+state;
    }
   
     public String UpdateHourFullClosing(){
        UpdateMinutes();
        UpdateState();
        UpdateHour();
        int hr=Integer.valueOf(hora)+7;
    
            if( hr==13){
              hora=Integer.toString(1);
              state="pm";
            }else if(hr==14){
                hora=Integer.toString(2);
                state="pm";
            }else if(hr==15){
                hora=Integer.toString(3);
                state="pm";
            }else if(hr==16){
                hora=Integer.toString(4);
                state="pm";
            }else if(hr==17){
                hora=Integer.toString(5);
                state="pm";
            }else if(hr==18){
                hora=Integer.toString(6);
            }else if(hr==19){
                state="pm";
                hora=Integer.toString(7);
            }else if(hr==20){
                hora=Integer.toString(8);
                state="pm";
            }else{
                hora=Integer.toString(hr);
                state="pm";
            } 
    return hora+":"+(minutos)+" "+state;
    }     
     
     public void updateDay(){
         c = new GregorianCalendar();
         dia = Integer.toString(c.get(Calendar.DATE));
     }
     
     public void updateMonth(){
         c = new GregorianCalendar();
         mes = Integer.toString(c.get(Calendar.MONTH)+1);
     }

     public void updateAge(){
         c = new GregorianCalendar();
         anio = Integer.toString(c.get(Calendar.YEAR)); 
     }
     
    public void mostrarVisualmenteFechaHora(){
        campo_Entrada.setText(UpdateHourFull());
        campo_Salida.setText(UpdateHourFullClosing());
 
        updateDay();
        updateMonth();
        updateAge();
        campo_Dia.setText(dia);
        campo_Mes.setText(mes);
        campo_Anio.setText(anio);
        
        etiqueta_Hora_actual.setText(UpdateHourFull());
    }

    public void insertarSalida(){
        Connection con = null;      
        try {
                con = DriverManager.getConnection(url);
                String nroId=String.valueOf(getNroID(con));
                PreparedStatement pst = con.prepareStatement( "UPDATE tabla_horas SET horaSalida=? WHERE id=?" );
                pst.setString(1,UpdateHourFull() );
                pst.setString(2,nroId );//"19"
                pst.executeUpdate();
                con.close();
        } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void insertaEntrada(){
        Connection con = null;
        try {
                con = DriverManager.getConnection(url);
                PreparedStatement pst = con.prepareStatement("INSERT INTO tabla_horas(dia,mes,anio,horaEntrada,dayString,monthString) VALUES(?, ?, ?, ?, ?, ?)" );
                pst.setString(1, campo_Dia.getText().trim() );
                pst.setString(2, campo_Mes.getText().trim() );
                pst.setString(3, campo_Anio.getText().trim() );
                pst.setString(4, UpdateHourFull() );
                pst.setString(5, diaDeLaSemana() );
                pst.setString(6, MesCadena() );
                pst.execute();
                
                pst.close();
                con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ocurrio error en la base de datos"+e.getMessage());
        }
    }
    
    public void insertarTodo(){
        Connection con = null;
        try {
                con = DriverManager.getConnection(url);
                PreparedStatement pst = con.prepareStatement("INSERT INTO tabla_horas(dia,mes,anio,horaEntrada,horaSalida,dayString,monthString) VALUES(?, ?, ?, ?, ?, ? ,?)" );
                pst.setString(1, campo_Dia.getText().trim() );
                pst.setString(2, campo_Mes.getText().trim() );
                pst.setString(3, campo_Anio.getText().trim() );
                pst.setString(4, campo_Entrada.getText().trim() );
                pst.setString(5, campo_Salida.getText().trim() );
                pst.setString(6, diaDeLaSemana() );
                pst.setString(7, MesCadena() );
                pst.execute();
                
                pst.close();
                con.close();
        } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
        } 
    }

    public int getNroID(Connection con){
        try{
                PreparedStatement  pst = con.prepareStatement("SELECT max(id) FROM tabla_horas");
                ResultSet rst = pst.executeQuery();
                while (rst.next()) {
                    return rst.getInt(1);
                }
                pst.close();
                con.close();
        }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
                return -1;
        }
        return 0;
    }
    
    public void  SqlVerDiaMesBusqueda(String idDia,String idMes){
        updateAge();
        Connection con = null;
        try{
                con = DriverManager.getConnection(url);    
                PreparedStatement pst = con.prepareStatement("select dayString, dia, monthString, anio,horaEntrada,horaSalida from tabla_horas where dia = '"+idDia+"' "+"and monthString = '"+idMes+"' "+"and anio = '"+anio+"' ");
                ResultSet rst = pst.executeQuery();
               
                String DIA="";
                while( rst.next() ){
                    DIA = rst.getString(1);
                    
                    campo_Area.append("  "+DIA+" "+rst.getString(2)+" de "+rst.getString(3)+" del "+rst.getString(4)+" \n");
                    campo_Area.append("  entrada :"+rst.getString(5)+"\n");
                    campo_Area.append("  salida :"+rst.getString(6)+"\n");
                    campo_Area.append("\n");
                    campo_Area.append("__________________________________________________________\n");
                }
                if(DIA.equals("")){
                      campo_Area.append("  !! No hay datos en el dia"+idDia+" del mes de "+idMes+" !! ");
                      campo_Area.append("\n");
                }
                con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ocurrio error en la base de datos"+e.getMessage());
        } 
    }

    public void  SqlVer(){
        Connection con = null;
        try{
                con = DriverManager.getConnection(url);    
                PreparedStatement pst = con.prepareStatement("SELECT dayString,dia,monthString,anio,horaEntrada,horaSalida FROM tabla_horas");
                ResultSet rst = pst.executeQuery();
                
                String DAY="";
                while( rst.next() ){
                    DAY = rst.getString(1);
                    campo_Area.append("  dia :"+DAY+" "+rst.getString(2)+"\n");
                    campo_Area.append("  mes :"+rst.getString(3)+"\n");
                    campo_Area.append("  anio :"+rst.getString(4)+"\n");
                    campo_Area.append("  entrada :"+rst.getString(5)+"\n");
                    campo_Area.append("  salida :"+rst.getString(6)+"\n");
                    campo_Area.append("\n");
                    campo_Area.append("_____________________________________________________________________\n");
                }
                if(DAY.equals("")){
                      campo_Area.append("  !! No hay datos en esta fecha !! ");
                      campo_Area.append("\n");
                }
                con.close();
        } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ocurrio error en la base de datos"+e.getMessage());
        } 
    }
    
    public String diaDeLaSemana(){
        c = new GregorianCalendar();
        int diaSemana = c.get(Calendar.DAY_OF_WEEK);
        if (diaSemana == 1) {
            return "Domingo";
        }else if(diaSemana == 2){
            return "Lunes";
        }else if(diaSemana == 3){
            return "Martes";
        }else if(diaSemana == 4){
            return "Miercoles";
        }else if (diaSemana == 5){
            return "Jueves";
        }else if (diaSemana == 6){
            return "Viernes";
        }else if (diaSemana == 7){
            return "Sabado";
        }else{
            return "ningun dia";
        }
    }
    
     public String MesCadena(){
        if (campo_Mes.getText().trim().equals("1")) {
            return "enero";
        }if(campo_Mes.getText().trim().equals("2")){
            return "febrero";
        } if(campo_Mes.getText().trim().equals("3")){
            return "marzo";
        } if(campo_Mes.getText().trim().equals("4")){
            return "abril";
        } if (campo_Mes.getText().trim().equals("5")){
            return "mayo";
        } if (campo_Mes.getText().trim().equals("6")){
            return "junio";
        } if (campo_Mes.getText().trim().equals("7")){
            return "julio";
        } if (campo_Mes.getText().trim().equals("8")){
            return "agosto";
        }  if (campo_Mes.getText().trim().equals("9")){
            return "septiembre";
        }  if (campo_Mes.getText().trim().equals("10")){
            return "octubre";
        }  if (campo_Mes.getText().trim().equals("11")){
            return "noviembre";
        }else if (campo_Mes.getText().trim().equals("12")){
            return "diciembre";
        }
    return "null";  
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JScrollPane();
        campo_Area = new javax.swing.JTextArea();
        campo_Entrada = new javax.swing.JTextField();
        campo_Salida = new javax.swing.JTextField();
        campo_Mes = new javax.swing.JTextField();
        ETIQUETA_MES = new javax.swing.JLabel();
        ETIQUETA_ENTRADA = new javax.swing.JLabel();
        ETIQUETA_SALIDA = new javax.swing.JLabel();
        boton_Entrada_ = new javax.swing.JButton();
        boton_Salida_ = new javax.swing.JButton();
        boton_Ver_ = new javax.swing.JButton();
        boton_Aceptar_ = new javax.swing.JButton();
        campo_Dia = new javax.swing.JTextField();
        campo_Anio = new javax.swing.JTextField();
        ETIQUETA_ANIO = new javax.swing.JLabel();
        ETIQUETA_DIA = new javax.swing.JLabel();
        campo_Secreto = new javax.swing.JLabel();
        ETIQUETA_BUSCAR_MES = new javax.swing.JLabel();
        cajaMes = new javax.swing.JComboBox();
        ETIQUETA_BUSCAR_DIA = new javax.swing.JLabel();
        cajaDia = new javax.swing.JComboBox();
        BOTON_BUSCAR_ = new javax.swing.JButton();
        etiqueta_Hora_actual = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(48, 16, 145));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        getContentPane().setLayout(null);

        campo_Area.setColumns(20);
        campo_Area.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        campo_Area.setRows(5);
        campo_Area.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 153)));
        campo_Area.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                campo_AreaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                campo_AreaMouseExited(evt);
            }
        });
        panel.setViewportView(campo_Area);

        getContentPane().add(panel);
        panel.setBounds(300, 20, 290, 320);

        campo_Entrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(campo_Entrada);
        campo_Entrada.setBounds(100, 20, 180, 30);

        campo_Salida.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campo_Salida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                campo_SalidaMouseClicked(evt);
            }
        });
        getContentPane().add(campo_Salida);
        campo_Salida.setBounds(100, 60, 180, 30);

        campo_Mes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                campo_MesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                campo_MesMouseExited(evt);
            }
        });
        getContentPane().add(campo_Mes);
        campo_Mes.setBounds(120, 110, 50, 40);

        ETIQUETA_MES.setForeground(new java.awt.Color(255, 255, 255));
        ETIQUETA_MES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ETIQUETA_MES.setText("Mes");
        getContentPane().add(ETIQUETA_MES);
        ETIQUETA_MES.setBounds(83, 110, 40, 40);

        ETIQUETA_ENTRADA.setForeground(new java.awt.Color(255, 255, 255));
        ETIQUETA_ENTRADA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ETIQUETA_ENTRADA.setText("Entrada");
        getContentPane().add(ETIQUETA_ENTRADA);
        ETIQUETA_ENTRADA.setBounds(50, 20, 50, 30);

        ETIQUETA_SALIDA.setForeground(new java.awt.Color(255, 255, 255));
        ETIQUETA_SALIDA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ETIQUETA_SALIDA.setText("Salida");
        getContentPane().add(ETIQUETA_SALIDA);
        ETIQUETA_SALIDA.setBounds(50, 60, 50, 30);

        boton_Entrada_.setBackground(new java.awt.Color(204, 255, 255));
        boton_Entrada_.setText("entrada");
        boton_Entrada_.setSelected(true);
        boton_Entrada_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton_Entrada_MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton_Entrada_MouseExited(evt);
            }
        });
        boton_Entrada_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_Entrada_ActionPerformed(evt);
            }
        });
        getContentPane().add(boton_Entrada_);
        boton_Entrada_.setBounds(20, 190, 120, 30);

        boton_Salida_.setBackground(new java.awt.Color(204, 255, 255));
        boton_Salida_.setText("salida");
        boton_Salida_.setSelected(true);
        boton_Salida_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton_Salida_MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton_Salida_MouseExited(evt);
            }
        });
        boton_Salida_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_Salida_ActionPerformed(evt);
            }
        });
        getContentPane().add(boton_Salida_);
        boton_Salida_.setBounds(20, 230, 120, 30);

        boton_Ver_.setBackground(new java.awt.Color(204, 255, 255));
        boton_Ver_.setText("Ver los horarios");
        boton_Ver_.setSelected(true);
        boton_Ver_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton_Ver_MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton_Ver_MouseExited(evt);
            }
        });
        boton_Ver_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_Ver_ActionPerformed(evt);
            }
        });
        getContentPane().add(boton_Ver_);
        boton_Ver_.setBounds(150, 200, 130, 50);

        boton_Aceptar_.setBackground(new java.awt.Color(204, 255, 255));
        boton_Aceptar_.setText("nuevo dia");
        boton_Aceptar_.setSelected(true);
        boton_Aceptar_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton_Aceptar_MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton_Aceptar_MouseExited(evt);
            }
        });
        boton_Aceptar_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_Aceptar_ActionPerformed(evt);
            }
        });
        getContentPane().add(boton_Aceptar_);
        boton_Aceptar_.setBounds(200, 160, 80, 20);

        campo_Dia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                campo_DiaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                campo_DiaMouseExited(evt);
            }
        });
        getContentPane().add(campo_Dia);
        campo_Dia.setBounds(40, 110, 40, 40);

        campo_Anio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                campo_AnioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                campo_AnioMouseExited(evt);
            }
        });
        getContentPane().add(campo_Anio);
        campo_Anio.setBounds(210, 110, 70, 40);

        ETIQUETA_ANIO.setForeground(new java.awt.Color(255, 255, 255));
        ETIQUETA_ANIO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ETIQUETA_ANIO.setText(" Año ");
        getContentPane().add(ETIQUETA_ANIO);
        ETIQUETA_ANIO.setBounds(170, 110, 40, 40);

        ETIQUETA_DIA.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ETIQUETA_DIA.setForeground(new java.awt.Color(255, 255, 255));
        ETIQUETA_DIA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ETIQUETA_DIA.setText("dia");
        getContentPane().add(ETIQUETA_DIA);
        ETIQUETA_DIA.setBounds(10, 110, 30, 40);

        campo_Secreto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        campo_Secreto.setForeground(new java.awt.Color(51, 255, 51));
        getContentPane().add(campo_Secreto);
        campo_Secreto.setBounds(20, 370, 340, 50);

        ETIQUETA_BUSCAR_MES.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ETIQUETA_BUSCAR_MES.setForeground(new java.awt.Color(255, 255, 255));
        ETIQUETA_BUSCAR_MES.setText("         mes");
        getContentPane().add(ETIQUETA_BUSCAR_MES);
        ETIQUETA_BUSCAR_MES.setBounds(90, 270, 100, 30);

        cajaMes.setBackground(new java.awt.Color(158, 153, 231));
        cajaMes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cajaMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre" }));
        cajaMes.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(cajaMes);
        cajaMes.setBounds(90, 300, 100, 40);

        ETIQUETA_BUSCAR_DIA.setBackground(new java.awt.Color(255, 255, 255));
        ETIQUETA_BUSCAR_DIA.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ETIQUETA_BUSCAR_DIA.setForeground(new java.awt.Color(255, 255, 255));
        ETIQUETA_BUSCAR_DIA.setText("      dia");
        getContentPane().add(ETIQUETA_BUSCAR_DIA);
        ETIQUETA_BUSCAR_DIA.setBounds(20, 270, 60, 30);

        cajaDia.setBackground(new java.awt.Color(158, 153, 231));
        cajaDia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        getContentPane().add(cajaDia);
        cajaDia.setBounds(20, 300, 60, 40);

        BOTON_BUSCAR_.setBackground(new java.awt.Color(204, 255, 255));
        BOTON_BUSCAR_.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        BOTON_BUSCAR_.setText("BUSCAR");
        BOTON_BUSCAR_.setSelected(true);
        BOTON_BUSCAR_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BOTON_BUSCAR_MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BOTON_BUSCAR_MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BOTON_BUSCAR_MouseExited(evt);
            }
        });
        BOTON_BUSCAR_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOTON_BUSCAR_ActionPerformed(evt);
            }
        });
        getContentPane().add(BOTON_BUSCAR_);
        BOTON_BUSCAR_.setBounds(200, 310, 80, 30);

        etiqueta_Hora_actual.setBackground(new java.awt.Color(0, 0, 0));
        etiqueta_Hora_actual.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        etiqueta_Hora_actual.setForeground(new java.awt.Color(255, 255, 255));
        etiqueta_Hora_actual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiqueta_Hora_actual.setText("  ");
        etiqueta_Hora_actual.setOpaque(true);
        getContentPane().add(etiqueta_Hora_actual);
        etiqueta_Hora_actual.setBounds(500, 360, 80, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents
  
    private void boton_Entrada_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_Entrada_ActionPerformed
        campo_Salida.setText("");
        insertaEntrada();
        JOptionPane.showMessageDialog(null, "Actulizado..(Entrada)!!!");
    }//GEN-LAST:event_boton_Entrada_ActionPerformed

    private void boton_Salida_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_Salida_ActionPerformed
        campo_Entrada.setText("");
        insertarSalida();
        JOptionPane.showMessageDialog(null, "Actulizado..(Salida)!!!");
    }//GEN-LAST:event_boton_Salida_ActionPerformed

    private void boton_Ver_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_Ver_ActionPerformed
        campo_Area.setText("");
        SqlVer();
    }//GEN-LAST:event_boton_Ver_ActionPerformed
       
    private void boton_Aceptar_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_Aceptar_ActionPerformed
       insertarTodo();
       JOptionPane.showMessageDialog(null, "Completado..(modificado)!!!");
    }//GEN-LAST:event_boton_Aceptar_ActionPerformed

    private void boton_Entrada_MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Entrada_MouseEntered
       campo_Entrada.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLUE));
       boton_Entrada_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.MAGENTA,3));
    }//GEN-LAST:event_boton_Entrada_MouseEntered

    private void boton_Entrada_MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Entrada_MouseExited
        campo_Entrada.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));
        boton_Entrada_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK,1));
    }//GEN-LAST:event_boton_Entrada_MouseExited

    private void boton_Salida_MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Salida_MouseEntered
        boton_Salida_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED,3));
        campo_Entrada.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED));
    }//GEN-LAST:event_boton_Salida_MouseEntered

    private void boton_Salida_MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Salida_MouseExited
        boton_Salida_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK,1));
        campo_Entrada.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));
    }//GEN-LAST:event_boton_Salida_MouseExited

    private void boton_Aceptar_MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Aceptar_MouseEntered
        boton_Aceptar_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GREEN,3));
    }//GEN-LAST:event_boton_Aceptar_MouseEntered

    private void boton_Aceptar_MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Aceptar_MouseExited
        boton_Aceptar_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK,1));
    }//GEN-LAST:event_boton_Aceptar_MouseExited

    private void boton_Ver_MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Ver_MouseEntered
        boton_Ver_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.ORANGE,3));
    }//GEN-LAST:event_boton_Ver_MouseEntered

    private void boton_Ver_MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_Ver_MouseExited
        boton_Ver_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK,1));
    }//GEN-LAST:event_boton_Ver_MouseExited

    private void campo_AreaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_AreaMouseEntered
        campo_Area.setBorder(new javax.swing.border.LineBorder(new Color(153,204,255), 3, true));
    }//GEN-LAST:event_campo_AreaMouseEntered

    private void campo_AreaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_AreaMouseExited
        campo_Area.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLUE));
    }//GEN-LAST:event_campo_AreaMouseExited

    private void campo_MesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_MesMouseEntered
        campo_Secreto.setText( diaDeLaSemana()+" "+dia+" de "+MesCadena()+" del "+anio );
        campo_Secreto.setVisible(true);
    }//GEN-LAST:event_campo_MesMouseEntered

    private void campo_MesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_MesMouseExited
        campo_Secreto.setText("");
        campo_Secreto.setVisible(false);
    }//GEN-LAST:event_campo_MesMouseExited

    private void campo_SalidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_SalidaMouseClicked
        campo_Salida.setText("");
    }//GEN-LAST:event_campo_SalidaMouseClicked

    private void BOTON_BUSCAR_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOTON_BUSCAR_ActionPerformed
        campo_Area.setText("");
        SqlVerDiaMesBusqueda(cajaDia.getSelectedItem().toString(), cajaMes.getSelectedItem().toString());
    }//GEN-LAST:event_BOTON_BUSCAR_ActionPerformed

    
    private void campo_AnioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_AnioMouseEntered
        campo_Secreto.setText( diaDeLaSemana()+" "+dia+" de "+MesCadena()+" del "+anio );
        campo_Secreto.setVisible(true);
    }//GEN-LAST:event_campo_AnioMouseEntered

    private void campo_AnioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_AnioMouseExited
         campo_Secreto.setText("");
        campo_Secreto.setVisible(false);
    }//GEN-LAST:event_campo_AnioMouseExited

    private void campo_DiaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_DiaMouseEntered
       campo_Secreto.setText( diaDeLaSemana()+" "+dia+" de "+MesCadena()+" del "+anio );
        campo_Secreto.setVisible(true);
    }//GEN-LAST:event_campo_DiaMouseEntered

    private void campo_DiaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campo_DiaMouseExited
         campo_Secreto.setText("");
        campo_Secreto.setVisible(false);
    }//GEN-LAST:event_campo_DiaMouseExited

    private void BOTON_BUSCAR_MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BOTON_BUSCAR_MouseExited
        BOTON_BUSCAR_.setBackground(new Color(204,255,255));
        BOTON_BUSCAR_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK,1));
    }//GEN-LAST:event_BOTON_BUSCAR_MouseExited

    private void BOTON_BUSCAR_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BOTON_BUSCAR_MouseClicked
        BOTON_BUSCAR_.setBackground(Color.yellow);
    }//GEN-LAST:event_BOTON_BUSCAR_MouseClicked

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        etiqueta_Hora_actual.setText(UpdateHourFull());
    }//GEN-LAST:event_formMouseEntered

    private void BOTON_BUSCAR_MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BOTON_BUSCAR_MouseEntered
       BOTON_BUSCAR_.setBorder(javax.swing.BorderFactory.createLineBorder(Color.YELLOW,3));
    }//GEN-LAST:event_BOTON_BUSCAR_MouseEntered
    
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Hour.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Hour.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Hour.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hour.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Hour().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BOTON_BUSCAR_;
    private javax.swing.JLabel ETIQUETA_ANIO;
    private javax.swing.JLabel ETIQUETA_BUSCAR_DIA;
    private javax.swing.JLabel ETIQUETA_BUSCAR_MES;
    private javax.swing.JLabel ETIQUETA_DIA;
    private javax.swing.JLabel ETIQUETA_ENTRADA;
    private javax.swing.JLabel ETIQUETA_MES;
    private javax.swing.JLabel ETIQUETA_SALIDA;
    private javax.swing.JButton boton_Aceptar_;
    private javax.swing.JButton boton_Entrada_;
    private javax.swing.JButton boton_Salida_;
    private javax.swing.JButton boton_Ver_;
    private javax.swing.JComboBox cajaDia;
    private javax.swing.JComboBox cajaMes;
    private javax.swing.JTextField campo_Anio;
    private javax.swing.JTextArea campo_Area;
    private javax.swing.JTextField campo_Dia;
    private javax.swing.JTextField campo_Entrada;
    private javax.swing.JTextField campo_Mes;
    private javax.swing.JTextField campo_Salida;
    private javax.swing.JLabel campo_Secreto;
    private javax.swing.JLabel etiqueta_Hora_actual;
    private javax.swing.JScrollPane panel;
    // End of variables declaration//GEN-END:variables

}
