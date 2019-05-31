package catering;

import catering.businesslogic.*;
import catering.businesslogic.Menu;
import catering.businesslogic.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuEditController {

    private boolean isEditingItem;

    @FXML
    private Label menuTitle;


    @FXML
    private ListView<Section> sectionsList;
    private ObservableList<Section> sections;

    @FXML
    private ListView<MenuItem> itemsList;
    private ObservableList<MenuItem> items;

    @FXML
    private Button deleteSectionButton;

    @FXML
    private Button editSectionButton;

    @FXML
    private Button moveUpSectionButton;

    @FXML
    private Button moveDownSectionButton;

    @FXML
    private Button addItemButton;

    @FXML
    private Button deleteItemButton;

    @FXML
    private Button editItemButton;

    @FXML
    private Button moveUpItemButton;

    @FXML
    private Button moveDownItemButton;

    @FXML
    private BorderPane sectionsPane;

    @FXML
    private BorderPane itemsPane;

    @FXML
    private BorderPane setItemPane;

    @FXML
    private BorderPane titlePane;

    @FXML
    private FlowPane bottomPane;

    @FXML
    private ComboBox<Recipe> recipesCombo;
    private ObservableList<Recipe> recipes;

    @FXML
    private ComboBox<Section> sectionsCombo;

    @FXML
    private TextArea itemDescription;

    @FXML
    private List<MenuEditListener> editListeners;

    public MenuEditController() {
        editListeners = new ArrayList<>();
    }

    public void initialize() {
        sectionsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        itemsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        sectionsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                int index = sectionsList.getSelectionModel().getSelectedIndex();
                selectSection(index);
            }
        });

        itemsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                int index = itemsList.getSelectionModel().getSelectedIndex();
                selectItem(index);
            }
        });
    }

    public void setup() {
        Menu m = CateringAppManager.menuManager.getCurrentMenu();

        String title = m.getTitle();
        if (title == null || title.trim().length() == 0) this.menuTitle.setText("(senza titolo)");
        else this.menuTitle.setText(title);

        this.resetSections();

        setItemPane.setVisible(false);
        setItemPane.setManaged(false);

        sectionsCombo.setItems(sections);
        List<Recipe> allRec = CateringAppManager.recipeManager.getRecipes();
        allRec.removeIf(recipe -> !recipe.isDish());
        recipes = FXCollections.observableList(allRec);
        recipesCombo.setItems(recipes);
    }

    private void resetSections() {

        sections = FXCollections.observableList(CateringAppManager.menuManager.getCurrentMenu().getSections());
        sections.add(0, new Section("(no section)"));

        sectionsList.setItems(sections);
        sectionsList.getFocusModel().focus(0);
        sectionsList.getSelectionModel().select(0);

        sectionsCombo.setItems(sections);
    }

    private void selectSection(int index) {
        if (index < 0) {
            items.clear();
            this.deleteSectionButton.setDisable(true);
            this.editSectionButton.setDisable(true);
            this.moveUpSectionButton.setDisable(true);
            this.moveDownSectionButton.setDisable(true);
        }
        else if (index == 0) { // no section
            items = FXCollections.observableList(CateringAppManager.menuManager.getCurrentMenu().getItemsWithoutSection());
            this.deleteSectionButton.setDisable(true);
            this.editSectionButton.setDisable(true);
            this.moveUpSectionButton.setDisable(true);
            this.moveDownSectionButton.setDisable(true);
        } else {
            items = FXCollections.observableList(this.sections.get(index).getItems());
            this.deleteSectionButton.setDisable(false);
            this.editSectionButton.setDisable(false);
            this.moveUpSectionButton.setDisable(index <= 1);
            this.moveDownSectionButton.setDisable(index >= sections.size()-1);
        }
        itemsList.getSelectionModel().clearSelection();
        itemsList.setItems(items);
        addItemButton.setDisable(false);
    }

    private void selectItem(int index) {
        if (index < 0){
            this.deleteItemButton.setDisable(true);
            this.editItemButton.setDisable(true);
            this.moveUpItemButton.setDisable(true);
            this.moveDownItemButton.setDisable(true);
        } else {
            this.deleteItemButton.setDisable(false);
            this.editItemButton.setDisable(false);
            this.moveUpItemButton.setDisable(index <= 0);
            this.moveDownItemButton.setDisable(index < 0 || index >= items.size() - 1);
        }
    }

    @FXML
    public void onAddSection() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Nuova sezione");
        dialog.setHeaderText("Come vuoi chiamare la sezione?");
        dialog.setContentText("");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            Section s = CateringAppManager.menuManager.defineSection(name);
            this.resetSections();
            this.sectionsList.getSelectionModel().select(s);
        });
    }

    @FXML
    public void onAddItem() {
        sectionsPane.setDisable(true);
        titlePane.setDisable(true);
        bottomPane.setDisable(true);

        int secIndex = sectionsList.getSelectionModel().getSelectedIndex();
        sectionsCombo.getSelectionModel().select((secIndex >= 0 ? secIndex: 0));
        recipesCombo.setDisable(false);
        recipesCombo.getSelectionModel().select(null);
        itemDescription.setText("");
        itemsPane.setVisible(false);
        itemsPane.setManaged(false);
        setItemPane.setManaged(true);
        setItemPane.setVisible(true);
        isEditingItem = false;

    }

    @FXML
    public void onCancelSetItem() {
        sectionsPane.setDisable(false);
        titlePane.setDisable(false);
        bottomPane.setDisable(false);

        recipesCombo.getSelectionModel().select(-1);
        sectionsCombo.getSelectionModel().select(-1);
        itemsPane.setVisible(true);
        itemsPane.setManaged(true);
        setItemPane.setManaged(false);
        setItemPane.setVisible(false);
    }

    @FXML
    public void onOkSetItem() {
        if (isEditingItem) {
            int oldSecIndex = sectionsList.getSelectionModel().getSelectedIndex();
            int newSecIndex = sectionsCombo.getSelectionModel().getSelectedIndex();
            MenuItem it = itemsList.getSelectionModel().getSelectedItem();
            if (oldSecIndex != newSecIndex) {
                Section theSec = null;
                if (newSecIndex > 0) {
                    theSec = sections.get(newSecIndex);
                }
                CateringAppManager.menuManager.assignItemToSection(it, theSec);
                this.sectionsList.getSelectionModel().select(newSecIndex);
                this.selectSection(newSecIndex);
                this.itemsList.getSelectionModel().select(it);
            }
            String newDesc = itemDescription.getText().trim();
            if (!newDesc.equals(it.getDescription().trim())) {
                CateringAppManager.menuManager.changeItemDescription(it, newDesc);
                this.itemsList.refresh();
            }
        }
        else {
            Recipe rec = recipesCombo.getSelectionModel().getSelectedItem();
            if (rec == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Devi scegliere una ricetta");
                alert.showAndWait();
            } else {
                int sectIndex = sectionsCombo.getSelectionModel().getSelectedIndex();
                String desc = itemDescription.getText();
                MenuItem it = null;
                if (sectIndex == 0) {
                    it = CateringAppManager.menuManager.insertItem(rec, null, desc);
                } else {
                    Section sec = sections.get(sectIndex);
                    it = CateringAppManager.menuManager.insertItem(rec, sec, desc);
                }
                this.sectionsList.getSelectionModel().select(sectIndex);
                this.selectSection(sectIndex);
                this.itemsList.getSelectionModel().select(it);
            }
        }
        onCancelSetItem();

    }

    @FXML
    private void onCloseMenu() {
        for (MenuEditListener l: editListeners) {
            l.onClose(false);
        }
    }

    @FXML
    private void onPublishMenu() {
        for (MenuEditListener l: editListeners) {
            l.onClose(true);
        }
    }

    public  void listen(MenuEditListener l) {
        editListeners.add(l);
    }

    @FXML
    private void onDeleteSection() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminazione di una sezione del menu");
        alert.setHeaderText(null);
        alert.setContentText("Vuoi eliminare anche le voci della sezione, o spostarle nel menu stesso?");

        ButtonType eliminaVoci = new ButtonType("Elimina");
        ButtonType spostaVoci = new ButtonType("Sposta");
        ButtonType annullaEliminazione = new ButtonType("Annulla");

        Section sec = sectionsList.getSelectionModel().getSelectedItem();

        alert.getButtonTypes().setAll(eliminaVoci, spostaVoci, annullaEliminazione);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == eliminaVoci){
            CateringAppManager.menuManager.deleteSectionWithItems(sec);
        } else if (result.get() == spostaVoci) {
            CateringAppManager.menuManager.deleteSection(sec);
        }
        this.resetSections();

    }

    @FXML
    private void onEditSection() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Modifica sezione");
        dialog.setHeaderText("Scrivi un nuovo nome per la sezione");
        Section sec = sectionsList.getSelectionModel().getSelectedItem();
        dialog.setContentText(sec.toString());
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            CateringAppManager.menuManager.changeSectionName(sec, name);
            sectionsList.refresh();
        });
    }

    @FXML
    private void onMoveUpSection() {
        int pos = sectionsList.getSelectionModel().getSelectedIndex()-1;
        Section sec = sections.get(pos+1);
        CateringAppManager.menuManager.moveSection(sec, pos-1);
        this.resetSections();
        sectionsList.getSelectionModel().select(sec);
    }

    @FXML private void onMoveDownSection() {
        int pos = sectionsList.getSelectionModel().getSelectedIndex()-1;
        Section sec = sections.get(pos+1);
        CateringAppManager.menuManager.moveSection(sec, pos+1);
        this.resetSections();
        sectionsList.getSelectionModel().select(sec);

    }

    @FXML
    private void onMoveUpItem() {
        int secPos = sectionsList.getSelectionModel().getSelectedIndex();
        int itPos = itemsList.getSelectionModel().getSelectedIndex();
        MenuItem it = itemsList.getSelectionModel().getSelectedItem();
        if (secPos == 0) {
            CateringAppManager.menuManager.moveItemsWithoutSection(it, itPos - 1);
        } else {
            CateringAppManager.menuManager.moveItemsInSection(sections.get(secPos), it, itPos-1);
        }
        this.selectSection(secPos);
        itemsList.getSelectionModel().select(it);
    }


    @FXML private void onMoveDownItem() {
        int secPos = sectionsList.getSelectionModel().getSelectedIndex();
        int itPos = itemsList.getSelectionModel().getSelectedIndex();
        MenuItem it = itemsList.getSelectionModel().getSelectedItem();
        if (secPos == 0) {
            CateringAppManager.menuManager.moveItemsWithoutSection(it, itPos + 1);
        } else {
            CateringAppManager.menuManager.moveItemsInSection(sections.get(secPos), it, itPos + 1);
        }
        this.selectSection(secPos);
        itemsList.getSelectionModel().select(it);
    }

    @FXML
    private void onEditItem()
    {
        sectionsPane.setDisable(true);
        titlePane.setDisable(true);
        bottomPane.setDisable(true);

        int secIndex = sectionsList.getSelectionModel().getSelectedIndex();
        sectionsCombo.getSelectionModel().select((secIndex >= 0 ? secIndex: 0));

        MenuItem item = itemsList.getSelectionModel().getSelectedItem();
        recipesCombo.getSelectionModel().select(item.getRecipe());
        recipesCombo.setDisable(true);

        itemDescription.setText(item.getDescription());

        itemsPane.setVisible(false);
        itemsPane.setManaged(false);
        setItemPane.setManaged(true);
        setItemPane.setVisible(true);
        isEditingItem = true;
    }

    @FXML
    private void onDeleteItem() {
        CateringAppManager.menuManager.deleteItem(itemsList.getSelectionModel().getSelectedItem());
        this.selectSection(this.sectionsList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void onEditMenuTitle() {
        String oldTitle = CateringAppManager.menuManager.getCurrentMenu().getTitle();
        TextInputDialog dialog = new TextInputDialog(oldTitle);
        dialog.setTitle("Cambia titolo");
        dialog.setHeaderText(null);
        dialog.setContentText("Inserisci un titolo per il menu:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(title -> {
            if (!title.equals(oldTitle))
                CateringAppManager.menuManager.setMenuTitle(title);
            this.menuTitle.setText(CateringAppManager.menuManager.getCurrentMenu().getTitle());
        });

    }

}