import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class GestionCommandeApp extends JFrame {

    private DefaultTableModel clientTableModel, fournisseurTableModel, produitTableModel, commandeTableModel;

    private JTextField clientNomField, clientPrenomField, clientCinField;

    private JTextField fournisseurNomField, fournisseurPrenomField, fournisseurRaisonSocialeField,
            fournisseurDomaineActiviteField;
    private JCheckBox fournisseurEstSocieteCheckBox;

    private JTextField produitLibelleField, produitPrixTTCField, produitQuantiteField;

    private JTextField commandeNumeroField, commandeQuantiteField;
    private JComboBox<String> commandeProduitComboBox, commandeClientComboBox, commandeFournisseurComboBox;

    private HashMap<Integer, Client> clientsMap = new HashMap<>();
    private List<Fournisseur> fournisseurs = new ArrayList<>();
    private List<Produit> produits = new ArrayList<>();
    private HashMap<Integer, Commande> commandesMap = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionCommandeApp app = new GestionCommandeApp();
            app.setVisible(true);
        });
    }

    private boolean isValidInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNonEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public GestionCommandeApp() {
        setTitle("Gestion des Commandes");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Clients", createClientPanel());
        tabbedPane.add("Fournisseurs", createFournisseurPanel());
        tabbedPane.add("Produits", createProduitPanel());
        tabbedPane.add("Commandes", createCommandePanel());

        add(tabbedPane, BorderLayout.CENTER);

        chargerClients();
        chargerFournisseurs();
        chargerProduits();
        chargerCommandes();
        updateClientList();
        updateFournisseurList();
        updateProduitList();
        updateCommandeList();

    }

    private JPanel createClientPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        clientTableModel = new DefaultTableModel(new String[]{"Nom", "Prénom", "CIN"}, 0);
        JTable clientTable = new JTable(clientTableModel);
        JScrollPane clientScrollPane = new JScrollPane(clientTable);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        clientNomField = new JTextField(20);
        clientPrenomField = new JTextField(20);
        clientCinField = new JTextField(10);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> addClient());

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> deleteClient(clientTable.getSelectedRow()));

        JTextField searchField = new JTextField(20);
        JComboBox<String> searchCriteriaComboBox = new JComboBox<>(new String[]{"Nom", "Prénom", "CIN"});
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            String selectedCriteria = (String) searchCriteriaComboBox.getSelectedItem();

            if (isNonEmpty(searchText)) {
                searchClients(searchText, selectedCriteria);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un critère de recherche.");
            }
        });

        JButton endSearchButton = new JButton("Terminer la recherche");
        endSearchButton.addActionListener(e -> updateClientList());

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(clientNomField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(clientPrenomField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("CIN:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(clientCinField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Action:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 2;
        inputPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Recherche Par :"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(searchCriteriaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Nom ou Prénom ou CIN du client:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(searchButton, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(endSearchButton, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(clientScrollPane, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createFournisseurPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        fournisseurTableModel = new DefaultTableModel(new String[]{"Nom", "Prénom", "Raison Sociale", "Domaine d'Activité", "Est Société"}, 0);
        JTable fournisseurTable = new JTable(fournisseurTableModel);
        JScrollPane fournisseurScrollPane = new JScrollPane(fournisseurTable);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        fournisseurNomField = new JTextField(20);
        fournisseurPrenomField = new JTextField(20);
        fournisseurRaisonSocialeField = new JTextField(20);
        fournisseurDomaineActiviteField = new JTextField(20);
        fournisseurEstSocieteCheckBox = new JCheckBox("Est Société");

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> addFournisseur());

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> deleteFournisseur(fournisseurTable.getSelectedRow()));

        JTextField searchField = new JTextField(20);
        JComboBox<String> searchCriteriaComboBox = new JComboBox<>(new String[]{"Nom", "Prénom", "Raison Sociale", "Domaine d'Activité"});
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            String selectedCriteria = (String) searchCriteriaComboBox.getSelectedItem();

            if (isNonEmpty(searchText)) {
                searchFournisseurs(searchText, selectedCriteria);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un critère de recherche.");
            }
        });

        JButton endSearchButton = new JButton("Terminer la recherche");
        endSearchButton.addActionListener(e -> updateFournisseurList());

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(fournisseurNomField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(fournisseurPrenomField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Raison Sociale:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(fournisseurRaisonSocialeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Domaine d'Activité:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(fournisseurDomaineActiviteField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(fournisseurEstSocieteCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Recherche Par :"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(searchCriteriaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Valeur à Chercher :"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(searchButton, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(endSearchButton, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(fournisseurScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void searchFournisseurs(String searchText, String criteria) {
        List<Fournisseur> results = new ArrayList<>();
        searchText = searchText.toLowerCase();

        for (Fournisseur fournisseur : fournisseurs) {
            String valueToCheck = "";
            switch (criteria) {
                case "Nom":
                    valueToCheck = fournisseur.getNom().toLowerCase();
                    break;
                case "Prénom":
                    valueToCheck = fournisseur.getPrenom().toLowerCase();
                    break;
                case "Raison Sociale":
                    valueToCheck = fournisseur.getRaisonSociale().toLowerCase();
                    break;
                case "Domaine d'Activité":
                    valueToCheck = fournisseur.getDomaineActivite().toLowerCase();
                    break;
                default:
                    break;
            }

            if (valueToCheck.contains(searchText)) {
                results.add(fournisseur);
            }
        }

        fournisseurTableModel.setRowCount(0);
        for (Fournisseur fournisseur : results) {
            fournisseurTableModel.addRow(new Object[]{
                    fournisseur.getNom(),
                    fournisseur.getPrenom(),
                    fournisseur.getRaisonSociale(),
                    fournisseur.getDomaineActivite(),
                    fournisseur.isEstSociete()
            });
        }
    }

    private JPanel createProduitPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        produitTableModel = new DefaultTableModel(new String[]{"Libellé", "Prix TTC", "Quantité"}, 0);
        JTable produitTable = new JTable(produitTableModel);
        JScrollPane produitScrollPane = new JScrollPane(produitTable);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        produitLibelleField = new JTextField(20);
        produitPrixTTCField = new JTextField(10);
        produitQuantiteField = new JTextField(10);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> addProduit());

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> deleteProduit(produitTable.getSelectedRow()));

        JTextField searchProduitField = new JTextField(20);
        JButton searchProduitButton = new JButton("Rechercher");
        searchProduitButton.addActionListener(e -> {
            String searchText = searchProduitField.getText();
            if (isNonEmpty(searchText)) {
                searchProduits(searchText);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un critère de recherche.");
            }
        });

        JButton endSearchProduitButton = new JButton("Terminer la recherche");
        endSearchProduitButton.addActionListener(e -> updateProduitList());

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Libellé:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(produitLibelleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Prix TTC:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(produitPrixTTCField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(produitQuantiteField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Actions:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 2;
        inputPanel.add(deleteButton, gbc);

        // Ajout des composants de recherche
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Recherche Par Libellé:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(searchProduitField, gbc);
        gbc.gridx = 2;
        inputPanel.add(searchProduitButton, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        inputPanel.add(endSearchProduitButton, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(produitScrollPane, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createCommandePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        commandeTableModel = new DefaultTableModel(
                new String[]{"Numéro", "Produits", "Quantité Totale", "Prix Total", "Date Commande", "Date Livraison", "Client", "Fournisseur"}, 0);
        JTable commandeTable = new JTable(commandeTableModel);
        JScrollPane commandeScrollPane = new JScrollPane(commandeTable);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        commandeNumeroField = new JTextField(20);
        commandeProduitComboBox = new JComboBox<>();
        commandeClientComboBox = new JComboBox<>();
        commandeFournisseurComboBox = new JComboBox<>();
        commandeQuantiteField = new JTextField(20);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> addCommande());

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> deleteCommande(commandeTable.getSelectedRow()));

        JTextField searchField = new JTextField(20);
        JComboBox<String> searchCriteriaComboBox = new JComboBox<>(new String[]{
                "Numéro de Commande", "Nom du Fournisseur", "Date de Commande", "Date de Livraison", "Nom du Client"
        });
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            String selectedCriteria = (String) searchCriteriaComboBox.getSelectedItem();

            if (isNonEmpty(searchText)) {
                searchCommandes(searchText, selectedCriteria);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un critère de recherche.");
            }
        });

        JButton endSearchButton = new JButton("Terminer la recherche");
        endSearchButton.addActionListener(e -> updateCommandeList());

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Numéro:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(commandeNumeroField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Produit:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(commandeProduitComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Client:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(commandeClientComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Fournisseur:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(commandeFournisseurComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(commandeQuantiteField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Recherche Par :"), gbc);
        gbc.gridx = 1;
        inputPanel.add(searchCriteriaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Valeur à Chercher :"), gbc);
        gbc.gridx = 1;
        inputPanel.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(searchButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(endSearchButton, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(commandeScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void searchCommandes(String searchText, String criteria) {
        List<Commande> results = new ArrayList<>();
        searchText = searchText.toLowerCase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Commande commande : commandesMap.values()) {
            String valueToCheck = "";
            switch (criteria) {
                case "Numéro de Commande":
                    valueToCheck = String.valueOf(commande.getNumero());
                    break;
                case "Nom du Fournisseur":
                    valueToCheck = (commande.getFournisseur() != null) ? commande.getFournisseur().getNom().toLowerCase() : "";
                    break;
                case "Date de Commande":
                    valueToCheck = (commande.getDateCommande() != null) ? dateFormat.format(commande.getDateCommande()).toLowerCase() : "";
                    break;
                case "Date de Livraison":
                    valueToCheck = (commande.getDateLivraison() != null) ? dateFormat.format(commande.getDateLivraison()).toLowerCase() : "";
                    break;
                case "Nom du Client":
                    valueToCheck = (commande.getClient() != null) ? commande.getClient().getNom().toLowerCase() : "";
                    break;
                case "Prénom du Client":
                    valueToCheck = (commande.getClient() != null) ? commande.getClient().getPrenom().toLowerCase() : "";
                    break;
                case "Nom ou Prénom du Client":
                    if (commande.getClient() != null) {
                        String nomClient = commande.getClient().getNom().toLowerCase();
                        valueToCheck = nomClient;
                    } else {
                        valueToCheck = "";
                    }
                    break;
                default:
                    break;
            }

            if (valueToCheck.contains(searchText)) {
                results.add(commande);
            }
        }

        updateCommandeTable(results);
    }

    private void updateCommandeTable(List<Commande> commandes) {
        commandeTableModel.setRowCount(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Commande commande : commandes) {
            String fournisseurNom = (commande.getFournisseur() != null) ? commande.getFournisseur().getNom() : "N/A";
            String clientNom = (commande.getClient() != null) ? commande.getClient().getNom() : "N/A";
            String produitLibelle = (commande.getProduitsListe() != null) ? commande.getProduitsListe().toString() : "N/A";

            commandeTableModel.addRow(new Object[]{
                    commande.getNumero(),
                    produitLibelle,
                    commande.getQuantiteTotale(),
                    commande.getPrixTotal(),
                    dateFormat.format(commande.getDateCommande()),
                    dateFormat.format(commande.getDateLivraison()),
                    clientNom,
                    fournisseurNom
            });
        }
    }


    private void searchClients(String searchText, String criteria) {
        List<Client> results = new ArrayList<>();
        searchText = searchText.toLowerCase();

        for (Client client : clientsMap.values()) {
            String valueToCheck = "";
            switch (criteria) {
                case "Nom":
                    valueToCheck = client.getNom().toLowerCase();
                    break;
                case "Prénom":
                    valueToCheck = client.getPrenom().toLowerCase();
                    break;
                case "CIN":
                    valueToCheck = String.valueOf(client.getCin());
                    break;
                default:
                    break;
            }

            if (valueToCheck.contains(searchText)) {
                results.add(client);
            }
        }


        clientTableModel.setRowCount(0);
        for (Client client : results) {
            clientTableModel.addRow(new Object[]{client.getNom(), client.getPrenom(), client.getCin()});
        }
    }

    private void addClient() {
        String nom = clientNomField.getText();
        String prenom = clientPrenomField.getText();
        String cinStr = clientCinField.getText();

        if (!isNonEmpty(nom) || !isNonEmpty(prenom) || !isValidInteger(cinStr)) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs valides pour le client.");
            return;
        }

        int cin = Integer.parseInt(cinStr);
        Client client = new Client(nom, prenom, cin);
        clientsMap.put(cin, client);
        updateClientList();
        sauvegarderClients();
        sauvegarderClientsTxt();
    }


    private void deleteClient(int rowIndex) {
        if (rowIndex >= 0) {
            int cin = Integer.parseInt(clientTableModel.getValueAt(rowIndex, 2).toString());
            clientsMap.remove(cin);
            updateClientList();
            sauvegarderClients();
            sauvegarderClientsTxt();
        }
    }

    private void addCommande() {
        String numeroStr = commandeNumeroField.getText();
        if (!isValidInteger(numeroStr) || commandeProduitComboBox.getSelectedIndex() < 0 ||
                commandeClientComboBox.getSelectedIndex() < 0 || !isValidInteger(commandeQuantiteField.getText())) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs valides pour la commande.");
            return;
        }
        int numero = Integer.parseInt(numeroStr);
        Produit produit = produits.get(commandeProduitComboBox.getSelectedIndex());
        Client client = clientsMap.get(Integer.parseInt(commandeClientComboBox.getSelectedItem().toString().split(" - ")[1]));
        Fournisseur fournisseur = null;
        if (commandeFournisseurComboBox.getSelectedIndex() != 0) {
            fournisseur = fournisseurs.get(commandeFournisseurComboBox.getSelectedIndex() - 1);
        }
        int quantite = Integer.parseInt(commandeQuantiteField.getText());
        if (!produit.isAvailable(quantite)) {
            JOptionPane.showMessageDialog(this, "Quantité insuffisante en stock.");
            return;
        }


        if (commandesMap.containsKey(numero)) {
            Commande commandeExistante = commandesMap.get(numero);
            commandeExistante.ajouterProduit(produit, quantite);
            produit.decrementStock(quantite);
            commandeExistante.setFournisseur(fournisseur);
        } else {

            String dateLivraisonString = JOptionPane.showInputDialog(this, "Entrez la date de livraison (jj/MM/yyyy) :");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateLivraison = null;
            try {
                dateLivraison = sdf.parse(dateLivraisonString);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez jj/MM/yyyy.");
                return;
            }
            Commande commande = new Commande(numero, client, dateLivraison);
            commande.ajouterProduit(produit, quantite);
            produit.decrementStock(quantite);
            commande.setFournisseur(fournisseur);
            commandesMap.put(numero, commande);
        }

        updateProduitList();
        updateCommandeList();
        sauvegarderCommandes();
        sauvegarderCommandesTxt();
        sauvegarderProduits();
        sauvegarderProduitsTxt();
    }


    private void addFournisseur() {
        String nom = fournisseurNomField.getText();
        String prenom = fournisseurPrenomField.getText();
        String raisonSociale = fournisseurRaisonSocialeField.getText();
        String domaineActivite = fournisseurDomaineActiviteField.getText();

        if (!isNonEmpty(nom) || !isNonEmpty(prenom) || !isNonEmpty(raisonSociale) || !isNonEmpty(domaineActivite)) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs valides pour le fournisseur.");
            return;
        }

        boolean estSociete = fournisseurEstSocieteCheckBox.isSelected();
        Fournisseur fournisseur = new Fournisseur(nom, prenom, raisonSociale, domaineActivite, estSociete);
        fournisseurs.add(fournisseur);
        updateFournisseurList();
        sauvegarderFournisseurs();
        sauvegarderFournisseursTxt();
    }

    private void deleteFournisseur(int rowIndex) {
        if (rowIndex >= 0) {
            fournisseurs.remove(rowIndex);
            updateFournisseurList();
            sauvegarderFournisseurs();
            sauvegarderFournisseursTxt();
        }
    }

    private void searchProduits(String searchText) {
        DefaultTableModel model = (DefaultTableModel) produitTableModel;
        model.setRowCount(0);

        for (Produit produit : produits) {
            if (produit.getLibelle().equalsIgnoreCase(searchText)) {
                model.addRow(new Object[]{produit.getLibelle(), produit.getPrixTTC(), produit.getStock()});
            }
        }
    }

    private void addProduit() {
        String libelle = produitLibelleField.getText();
        String prixTTCStr = produitPrixTTCField.getText();
        String quantiteStr = produitQuantiteField.getText();

        if (!isNonEmpty(libelle) || !isValidDouble(prixTTCStr) || !isValidInteger(quantiteStr)) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs valides pour le produit.");
            return;
        }

        double prixTTC = Double.parseDouble(prixTTCStr);
        int quantite = Integer.parseInt(quantiteStr);

        Produit produit = new Produit(libelle, prixTTC, quantite);
        produits.add(produit);
        updateProduitList();
        sauvegarderProduits();
        sauvegarderProduitsTxt();
    }


    private void deleteProduit(int rowIndex) {
        if (rowIndex >= 0) {
            produits.remove(rowIndex);
            updateProduitList();
            sauvegarderProduits();
            sauvegarderProduitsTxt();
        }
    }


    private void deleteCommande(int rowIndex) {
        if (rowIndex >= 0) {
            int numero = Integer.parseInt(commandeTableModel.getValueAt(rowIndex, 0).toString());
            commandesMap.remove(numero);
            updateCommandeList();
            sauvegarderCommandes();
            sauvegarderCommandesTxt();
        }
    }

    private void updateClientList() {
        clientTableModel.setRowCount(0);
        for (Client client : clientsMap.values()) {
            clientTableModel.addRow(new Object[]{client.getNom(), client.getPrenom(), client.getCin()});
            commandeClientComboBox.addItem(client.getNom() + " " + client.getPrenom() + " - " + client.getCin());
        }
    }


    private void updateFournisseurList() {
        fournisseurTableModel.setRowCount(0);
        commandeFournisseurComboBox.removeAllItems();
        commandeFournisseurComboBox.addItem("None");
        for (Fournisseur fournisseur : fournisseurs) {
            fournisseurTableModel.addRow(new Object[]{
                    fournisseur.getNom(),
                    fournisseur.getPrenom(),
                    fournisseur.getRaisonSociale(),
                    fournisseur.getDomaineActivite(),
                    fournisseur.isEstSociete()
            });
            commandeFournisseurComboBox.addItem(fournisseur.getNom() + " " + fournisseur.getPrenom());
        }
    }

    private void updateProduitList() {
        produitTableModel.setRowCount(0);
        commandeProduitComboBox.removeAllItems();
        for (Produit produit : produits) {
            produitTableModel.addRow(new Object[]{produit.getLibelle(), produit.getPrixTTC(), produit.getStock()});
            commandeProduitComboBox.addItem(produit.getLibelle());
        }
    }


    private void updateCommandeList() {
        commandeTableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Commande commande : commandesMap.values()) {
            String produitsListe = String.join(", ", commande.getProduitsListe());
            commandeTableModel.addRow(new Object[]{
                    commande.getNumero(),
                    produitsListe,
                    commande.getQuantiteTotale(),
                    commande.getPrixTotal(),
                    sdf.format(commande.getDateCommande()),
                    commande.getDateLivraison() != null ? sdf.format(commande.getDateLivraison()) : "Non définie",
                    commande.getClient().getNom() + " " + commande.getClient().getPrenom(),
                    commande.getFournisseur() == null ? "None" : commande.getFournisseur().getNom() + " " + commande.getFournisseur().getPrenom()
            });
        }
    }


    private void chargerClients() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clients.dat"))) {
            clientsMap = (HashMap<Integer, Client>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderClients() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("clients.dat"))) {
            oos.writeObject(clientsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderClientsTxt() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("clients.txt"))) {
            for (Client client : clientsMap.values()) {
                writer.println(client.getNom() + "," + client.getPrenom() + "," + client.getCin());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerFournisseurs() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("fournisseurs.dat"))) {
            fournisseurs = (List<Fournisseur>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderFournisseurs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("fournisseurs.dat"))) {
            oos.writeObject(fournisseurs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderFournisseursTxt() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("fournisseurs.txt"))) {
            for (Fournisseur fournisseur : fournisseurs) {
                writer.println(fournisseur.getNom() + "," + fournisseur.getPrenom() + "," +
                        fournisseur.getRaisonSociale() + "," + fournisseur.getDomaineActivite() + "," + fournisseur.isEstSociete());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerProduits() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("produits.dat"))) {
            produits = (List<Produit>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderProduits() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("produits.dat"))) {
            oos.writeObject(produits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderProduitsTxt() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("produits.txt"))) {
            for (Produit produit : produits) {
                writer.println(produit.getLibelle() + "," + produit.getPrixTTC() + "," + produit.getStock());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void chargerCommandes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("commandes.dat"))) {
            commandesMap = (HashMap<Integer, Commande>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderCommandes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("commandes.dat"))) {
            oos.writeObject(commandesMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderCommandesTxt() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("commandes.txt"))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Commande commande : commandesMap.values()) {
                String produitsListe = String.join(", ", commande.getProduitsListe());
                writer.println(commande.getNumero() + "," + produitsListe + "," + commande.getQuantiteTotale() + "," +
                        commande.getPrixTotal() + "," + sdf.format(commande.getDateCommande()) + "," +
                        sdf.format(commande.getDateLivraison()) + "," +
                        commande.getClient().getNom() + " " + commande.getClient().getPrenom() + "," +
                        (commande.getFournisseur() == null ? "None" : commande.getFournisseur().getNom() + " " + commande.getFournisseur().getPrenom()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}