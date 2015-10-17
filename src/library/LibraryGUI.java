/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LibraryGUI.java
 *
 * Created on 28-Sep-2009, 11:55:39
 */
package library;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kutoma
 */
public class LibraryGUI extends javax.swing.JFrame {

    private SetOfMembers theMembers = new SetOfMembers();
    private SetOfBooks holdings = new SetOfBooks();

    private Member selectedMember;
    private Book selectedBorrowedBook;
    private Book selectedLibraryBook;

    private boolean membersAreBeingFiltered = false;
    private SetOfMembers filteredMembers = new SetOfMembers();

    private boolean libraryisBeingFiltered = false;
    private SetOfBooks filteredBooks = new SetOfBooks();

    private SetOfMembers getMembers() {
        return membersAreBeingFiltered ? filteredMembers : theMembers;
    }

    private SetOfBooks getBooks() {
        return libraryisBeingFiltered ? filteredBooks : holdings;
    }

    /**
     * Creates new form LibraryGUI
     */
    public LibraryGUI() {
        System.out.println("Starting");
        initComponents();
        setupListeners();
        setupData();

        reloadLibraryTable();
        reloadMembersTable();
    }

    private void setupData() {
        Member member1 = new Member("Jane");
        Member member2 = new Member("Amir");
        Member member3 = new Member("Astrid");
        Member member4 = new Member("Andy");

        theMembers.addMember(member1);
        theMembers.addMember(member2);
        theMembers.addMember(member3);
        theMembers.addMember(member4);

        Book book1 = new Book("book1", "Tolstoj", "isnb8732784");
        Book book2 = new Book("book2", "rowel dahl", "idn382973");
        Book book3 = new Book("book3", "Si mccable", "ief3298739");
        Book book4 = new Book("book4", "dale bundy", "kenf9u2uenf");
        Book book5 = new Book("book5", "will cox", "2fjnoenr");

        member1.borrowBook(book2);
        member1.borrowBook(book3);

        holdings.addBook(book1);
        holdings.addBook(book2);
        holdings.addBook(book3);
        holdings.addBook(book4);
        holdings.addBook(book5);
    }

    private void setupListeners() {
        tbl_members.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tbl_members.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    selectedMember = getMembers().get(tbl_members.getSelectedRow());
                    loadLoanedBooksForMember(selectedMember);
                }
            }
        });

        tbl_borrowedBooks.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tbl_borrowedBooks.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    Book book;
                    book = selectedMember.getBooksOnLoan()[row];
                    selectedBorrowedBook = book;
                }
            }
        });

        tbl_library.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tbl_library.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    selectedLibraryBook = holdings.availableBooks().get(row);
                }
            }
        });

        DocumentListener membersDocumentListener = new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                filterMembers();
                reloadMembersTable();
            }

            public void removeUpdate(DocumentEvent e) {
                if (txt_memberName.getText().length() == 0
                        && txt_memberNumber.getText().length() == 0) {
                    membersAreBeingFiltered = false;
                } else {
                    filteredMembers = theMembers;
                    filterMembers();
                }
                reloadMembersTable();
            }

        };

        txt_memberName.getDocument().addDocumentListener(membersDocumentListener);
        txt_memberNumber.getDocument().addDocumentListener(membersDocumentListener);

        DocumentListener libraryDocumentListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                filterLibrary();
                reloadLibraryTable();
            }

            public void removeUpdate(DocumentEvent e) {
                if (txt_bookAuthor.getText().length() == 0
                        && txt_bookTitle.getText().length() == 0
                        && txt_bookAccNumber.getText().length() == 0
                        && txt_bookISBN.getText().length() == 0) {
                    libraryisBeingFiltered = false;
                } else {
                    filteredBooks = holdings;
                    filterLibrary();
                }
                reloadLibraryTable();
            }
        };
        txt_bookAccNumber.getDocument().addDocumentListener(libraryDocumentListener);
        txt_bookAuthor.getDocument().addDocumentListener(libraryDocumentListener);
        txt_bookISBN.getDocument().addDocumentListener(libraryDocumentListener);
        txt_bookTitle.getDocument().addDocumentListener(libraryDocumentListener);
    }

    private void filterMembers() {
        if (txt_memberName.getText().length() > 0) {
            filteredMembers = getMembers().getMemberFromName(txt_memberName.getText());
        }
        if (txt_memberNumber.getText().length() > 0) {
            filteredMembers = getMembers().getMemberFromNumber(txt_memberNumber.getText());
        }
        membersAreBeingFiltered = true;
    }

    private void filterLibrary() {
        if (txt_bookAuthor.getText().length() > 0) {
            filteredBooks = getBooks().findBookByAuthor(txt_bookAuthor.getText());
        }
        if (txt_bookTitle.getText().length() > 0) {
            filteredBooks = getBooks().findBookByTitle(txt_bookTitle.getText());
        }
        if (txt_bookAccNumber.getText().length() > 0) {
            filteredBooks = getBooks().findBookByAccNum(txt_bookAccNumber.getText());
        }
        if (txt_bookISBN.getText().length() > 0) {
            filteredBooks = getBooks().findBookByISBN(txt_bookISBN.getText());
        }
        libraryisBeingFiltered = true;
    }

    private boolean approveLoan() {
        return selectedMember.getBooksOnLoan().length < 3;
    }

    private void loadLoanedBooksForMember(Member aMember) {
        DefaultTableModel model = (DefaultTableModel) tbl_borrowedBooks.getModel();

        model.setRowCount(0);
        selectedBorrowedBook = null;

        if (aMember != null) {
            for (Book book : aMember.getBooksOnLoan()) {
                model.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getISBNNumber(), book.getAccessionNumber()});
            }
        }
    }

    private void reloadMembersTable() {
        DefaultTableModel membersModel = (DefaultTableModel) tbl_members.getModel();

        membersModel.setRowCount(0);
        selectedMember = null;

        for (Member member : getMembers()) {
            membersModel.addRow(new Object[]{member.getMemberNumber(), member.getName()});
        }
    }

    private void reloadLibraryTable() {
        DefaultTableModel libraryModel = (DefaultTableModel) tbl_library.getModel();

        libraryModel.setRowCount(0);
        selectedLibraryBook = null;

        for (Book book : getBooks().availableBooks()) {
            libraryModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getISBNNumber(), book.getAccessionNumber()});
        }
    }

    private void saveLibraryBooks() {
        try {
            FileOutputStream fout = new FileOutputStream("libraryBooks.science");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(holdings);
            oos.close();
            System.out.println("Saved books");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void saveMembers() {
        try {
            FileOutputStream fout = new FileOutputStream("members.science");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(theMembers);
            oos.close();
            System.out.println("Saved members");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txt_memberName = new javax.swing.JTextField();
        txt_memberNumber = new javax.swing.JTextField();
        btn_addNewMember = new javax.swing.JButton();
        btn_removeMember = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_members = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_newMemberName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btn_returnBook = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_borrowedBooks = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        txt_bookAuthor = new javax.swing.JTextField();
        txt_bookTitle = new javax.swing.JTextField();
        txt_bookAccNumber = new javax.swing.JTextField();
        txt_bookISBN = new javax.swing.JTextField();
        btn_loanBook = new javax.swing.JButton();
        btn_removeBookFromLibrary = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_library = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btn_saveAndExit = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txt_addBookTitle = new javax.swing.JTextField();
        txt_addBookAuthor = new javax.swing.JTextField();
        txt_addBookISBN = new javax.swing.JTextField();
        btn_addToLibrary = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));

        btn_addNewMember.setText("Add New Member");
        btn_addNewMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addNewMemberActionPerformed(evt);
            }
        });

        btn_removeMember.setText("Remove Member");
        btn_removeMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_removeMemberActionPerformed(evt);
            }
        });

        tbl_members.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member Number", "Name"
            }
        ));
        tbl_members.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(tbl_members);

        jLabel1.setText("Name: ");

        jLabel2.setText("Number:");

        jLabel10.setText("Name:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btn_removeMember, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_memberName)
                            .addComponent(txt_memberNumber)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_newMemberName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_addNewMember)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_memberName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_memberNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_addNewMember)
                    .addComponent(txt_newMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_removeMember)
                .addContainerGap())
        );

        btn_returnBook.setText("Return Book");
        btn_returnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_returnBookActionPerformed(evt);
            }
        });

        tbl_borrowedBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Author", "ISBN", "Acc Number"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_borrowedBooks.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tbl_borrowedBooks);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btn_returnBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_returnBook)
                .addGap(10, 10, 10))
        );

        btn_loanBook.setText("Loan Book");
        btn_loanBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loanBookActionPerformed(evt);
            }
        });

        btn_removeBookFromLibrary.setText("Remove Book From Library");
        btn_removeBookFromLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_removeBookFromLibraryActionPerformed(evt);
            }
        });

        tbl_library.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Author", "ISBN", "Acc Number"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tbl_library);

        jLabel6.setText("Author:");

        jLabel7.setText("Title:");

        jLabel8.setText("Acc Num:");

        jLabel9.setText("ISBN:");

        btn_saveAndExit.setText("Save and Exit");
        btn_saveAndExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveAndExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_bookAccNumber)
                                    .addComponent(txt_bookISBN)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_bookTitle)
                                    .addComponent(txt_bookAuthor)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_removeBookFromLibrary, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(btn_loanBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_saveAndExit)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_bookAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_bookTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_bookAccNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_bookISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_loanBook)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_removeBookFromLibrary))
                    .addComponent(btn_saveAndExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btn_addToLibrary.setText("Add to Library");
        btn_addToLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addToLibraryActionPerformed(evt);
            }
        });

        jLabel3.setText("Title:");

        jLabel4.setText("Author:");

        jLabel5.setText("ISBN:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_addToLibrary, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_addBookISBN, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_addBookAuthor, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_addBookTitle, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_addBookTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_addBookAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_addBookISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_addToLibrary)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_addToLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addToLibraryActionPerformed
        String title = txt_addBookTitle.getText();
        String author = txt_addBookAuthor.getText();
        String iSBN = txt_addBookISBN.getText();

        if (title.length() > 0 && author.length() > 0 && iSBN.length() > 0) {
            txt_addBookTitle.setText("");
            txt_addBookAuthor.setText("");
            txt_addBookISBN.setText("");

            holdings.addBook(new Book(title, author, iSBN));

            reloadLibraryTable();
        } else {
            JOptionPane.showMessageDialog(null, "Missing information for book", "Warning", JOptionPane.PLAIN_MESSAGE);
        }
    }//GEN-LAST:event_btn_addToLibraryActionPerformed

    private void btn_returnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_returnBookActionPerformed

        if (selectedBorrowedBook == null) {
            JOptionPane.showMessageDialog(null, "No book selected", "Warning", JOptionPane.PLAIN_MESSAGE);
        } else {
            selectedMember.returnBook(selectedBorrowedBook);
            loadLoanedBooksForMember(selectedMember);
            reloadLibraryTable();
        }
    }//GEN-LAST:event_btn_returnBookActionPerformed

    private void btn_loanBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loanBookActionPerformed

        if (selectedLibraryBook == null || selectedMember == null) {
            JOptionPane.showMessageDialog(null, "Please select a member and a book", "Warning", JOptionPane.PLAIN_MESSAGE);
        } else if (!approveLoan()) {
            JOptionPane.showMessageDialog(null, "Maximum number of books already on loan", "Warning", JOptionPane.PLAIN_MESSAGE);
        } else {
            selectedMember.borrowBook(selectedLibraryBook);
            loadLoanedBooksForMember(selectedMember);
            reloadLibraryTable();
        }
    }//GEN-LAST:event_btn_loanBookActionPerformed

    private void btn_addNewMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addNewMemberActionPerformed
        String name = txt_newMemberName.getText();
        txt_newMemberName.setText("");
        if (name.length() > 0) {
            theMembers.add(new Member(name));
        }
        reloadMembersTable();
        loadLoanedBooksForMember(null);
    }//GEN-LAST:event_btn_addNewMemberActionPerformed

    private void btn_removeMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removeMemberActionPerformed
        if (selectedMember == null) {
            JOptionPane.showMessageDialog(null, "Please select a member", "Warning", JOptionPane.PLAIN_MESSAGE);
        } else if (selectedMember.getBooksOnLoan().length > 0) {
            JOptionPane.showMessageDialog(null, "You can't delete this user because they have books on loan.", "Warning", JOptionPane.PLAIN_MESSAGE);
        } else {
            theMembers.removeMember(selectedMember);
            selectedMember = null;
            reloadMembersTable();
            loadLoanedBooksForMember(null);
        }
    }//GEN-LAST:event_btn_removeMemberActionPerformed

    private void btn_removeBookFromLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removeBookFromLibraryActionPerformed

        if (selectedLibraryBook == null) {
            JOptionPane.showMessageDialog(null, "No book selected", "Warning", JOptionPane.PLAIN_MESSAGE);
        } else {
            holdings.removeBook(selectedLibraryBook);
            reloadLibraryTable();
        }

    }//GEN-LAST:event_btn_removeBookFromLibraryActionPerformed

    private void btn_saveAndExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveAndExitActionPerformed
        saveLibraryBooks();
        saveMembers();
        System.exit(0);
    }//GEN-LAST:event_btn_saveAndExitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LibraryGUI().setVisible(true);
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_addNewMember;
    private javax.swing.JButton btn_addToLibrary;
    private javax.swing.JButton btn_loanBook;
    private javax.swing.JButton btn_removeBookFromLibrary;
    private javax.swing.JButton btn_removeMember;
    private javax.swing.JButton btn_returnBook;
    private javax.swing.JButton btn_saveAndExit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable tbl_borrowedBooks;
    private javax.swing.JTable tbl_library;
    private javax.swing.JTable tbl_members;
    private javax.swing.JTextField txt_addBookAuthor;
    private javax.swing.JTextField txt_addBookISBN;
    private javax.swing.JTextField txt_addBookTitle;
    private javax.swing.JTextField txt_bookAccNumber;
    private javax.swing.JTextField txt_bookAuthor;
    private javax.swing.JTextField txt_bookISBN;
    private javax.swing.JTextField txt_bookTitle;
    private javax.swing.JTextField txt_memberName;
    private javax.swing.JTextField txt_memberNumber;
    private javax.swing.JTextField txt_newMemberName;
    // End of variables declaration//GEN-END:variables

}
