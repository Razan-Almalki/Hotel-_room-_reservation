package hotel_room_reservation;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class NewJFrame extends javax.swing.JFrame {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8800;

    public NewJFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        View_Rooms = new javax.swing.JButton();
        Make_Reservation = new javax.swing.JButton();
        View_Reservations = new javax.swing.JButton();
        Cancel_Reservation = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        View_Rooms.setBackground(new java.awt.Color(255, 255, 176));
        View_Rooms.setFont(new java.awt.Font("Bodoni MT", 0, 18)); // NOI18N
        View_Rooms.setForeground(new java.awt.Color(0, 0, 102));
        View_Rooms.setText("View Rooms");
        View_Rooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View_RoomsActionPerformed(evt);
            }
        });

        Make_Reservation.setBackground(new java.awt.Color(255, 255, 157));
        Make_Reservation.setFont(new java.awt.Font("Bodoni MT", 0, 18)); // NOI18N
        Make_Reservation.setForeground(new java.awt.Color(0, 0, 102));
        Make_Reservation.setText("Make Reservation");
        Make_Reservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Make_ReservationActionPerformed(evt);
            }
        });

        View_Reservations.setBackground(new java.awt.Color(255, 255, 136));
        View_Reservations.setFont(new java.awt.Font("Bodoni MT", 0, 18)); // NOI18N
        View_Reservations.setForeground(new java.awt.Color(0, 0, 102));
        View_Reservations.setText("View Reservations");
        View_Reservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View_ReservationsActionPerformed(evt);
            }
        });

        Cancel_Reservation.setBackground(new java.awt.Color(255, 255, 114));
        Cancel_Reservation.setFont(new java.awt.Font("Bodoni MT", 0, 18)); // NOI18N
        Cancel_Reservation.setForeground(new java.awt.Color(0, 0, 102));
        Cancel_Reservation.setText("Cancel Reservation");
        Cancel_Reservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ReservationActionPerformed(evt);
            }
        });

        Exit.setBackground(new java.awt.Color(102, 102, 0));
        Exit.setFont(new java.awt.Font("Bodoni MT", 0, 18)); // NOI18N
        Exit.setForeground(new java.awt.Color(0, 0, 102));
        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon("D:\\Backup\\Users\\Roby\\Documents\\NetBeansProjects\\Hotel_room_reservation\\Screenshot 2023-05-23 075236.png")); // NOI18N
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(997, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(Cancel_Reservation, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                        .addComponent(View_Reservations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Make_Reservation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(View_Rooms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(74, 74, 74))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 44, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(View_Rooms, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Make_Reservation, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(View_Reservations, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Cancel_Reservation, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void View_RoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View_RoomsActionPerformed
        try {
            Hotel_room_reservation.viewRooms();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_View_RoomsActionPerformed

    private void Make_ReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Make_ReservationActionPerformed
        if (JOptionPane.CANCEL_OPTION == 0) {
            System.exit(0);
        }
        try {
            Hotel_room_reservation.makeReservation();
        } catch (ReservationException ex) {
            ex.getMessage();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_Make_ReservationActionPerformed

    private void View_ReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View_ReservationsActionPerformed
        if (JOptionPane.CANCEL_OPTION == 0) {
            System.exit(0);
        }
        try {
            Hotel_room_reservation.viewReservations();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_View_ReservationsActionPerformed

    private void Cancel_ReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ReservationActionPerformed
        if (JOptionPane.CANCEL_OPTION == 0) {
            System.exit(0);
        }
        try {
            Hotel_room_reservation.cancelReservation();
        } catch (ReservationException ex) {
            ex.getMessage();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }


    }//GEN-LAST:event_Cancel_ReservationActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed

        JOptionPane.showMessageDialog(null, "Thank You! visit us soon ...");
        System.exit(0);

    }//GEN-LAST:event_ExitActionPerformed

    public static void main(String args[]) {

        try {

            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel_Reservation;
    private javax.swing.JButton Exit;
    private javax.swing.JButton Make_Reservation;
    private javax.swing.JButton View_Reservations;
    private javax.swing.JButton View_Rooms;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
