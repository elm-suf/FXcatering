package catering.persistence;

import catering.businesslogic.grasp_controllers.*;
import catering.businesslogic.managers.CateringAppManager;
import catering.businesslogic.receivers.CatEventReceiver;
import catering.businesslogic.receivers.MenuEventReceiver;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DataManager {
    private String userName = "root";
    private String password = "root";
    private String serverName = "localhost";
    private String portNumber = "3306";


    private Connection connection;

    // Il DataManager deve tener traccia di quali oggetti in memoria
    // corrispondono a quali record del DB. Per questo usa una doppia
    // mappa per ciascun tipo di oggetto caricato
    private Map<User, Integer> userObjects;
    private Map<Integer, User> idToUserObject;

    private Map<Shift, Integer> shiftObjects;
    private Map<Integer, Shift> idToShiftObject;

    private Map<Recipe, Integer> recipeObjects;
    private Map<Integer, Recipe> idToRecipeObject;

    private Map<Menu, Integer> menuObjects;
    private Map<Integer, Menu> idToMenuObject;

    private Map<Section, Integer> sectionObjects;
    private Map<Integer, Section> idToSectionObject;

    private Map<MenuItem, Integer> itemObjects;
    private Map<Integer, MenuItem> idToItemObject;

    public DataManager() {

        this.userObjects = new HashMap<>();
        this.idToUserObject = new HashMap<>();
        this.shiftObjects = new HashMap<>();
        this.idToShiftObject = new HashMap<>();
        this.recipeObjects = new HashMap<>();
        this.idToRecipeObject = new HashMap<>();
        this.menuObjects = new HashMap<>();
        this.idToMenuObject = new HashMap<>();
        this.sectionObjects = new HashMap<>();
        this.idToSectionObject = new HashMap<>();
        this.itemObjects = new HashMap<>();
        this.idToItemObject = new HashMap<>();


    }

    public void initialize() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        connectionProps.put("useUnicode", true);
        connectionProps.put("useJDBCCompliantTimezoneShift", true);
        connectionProps.put("useLegacyDatetimeCode", false);
        connectionProps.put("serverTimezone", "UTC");

//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


        conn = DriverManager.getConnection(
                "jdbc:mysql://" +
                        this.serverName +
                        ":" + this.portNumber + "/catering",
                connectionProps);

        System.out.println("Connected to database");
        this.connection = conn;


        CateringAppManager.menuManager.addReceiver(new MenuEventReceiver() {
            @Override
            public void notifyMenuCreated(Menu m) {
                int mid = writeNewMenu(m);
                menuObjects.put(m, mid);
                idToMenuObject.put(mid, m);
                List<Section> secs = m.getSections();
                for (int i = 0; i < secs.size(); i++) {
                    Section s = secs.get(i);
                    int sid = writeNewSection(mid, i, s);
                    sectionObjects.put(s, sid);
                    idToSectionObject.put(sid, s);

                    List<MenuItem> secItems = s.getItems();

                    for (int j = 0; j < secItems.size(); j++) {
                        MenuItem it = secItems.get(j);
                        int iid = writeNewItem(mid, sid, j, it);
                        itemObjects.put(it, iid);
                        idToItemObject.put(iid, it);
                    }
                }

                List<MenuItem> menuItems = m.getItemsWithoutSection();
                for (int z = 0; z < menuItems.size(); z++) {
                    MenuItem it = menuItems.get(z);
                    int iid = writeNewItem(mid, 0, z, it);
                    itemObjects.put(it, iid);
                    idToItemObject.put(iid, it);
                }
            }

            @Override
            public void notifySectionAdded(Menu m, Section s) {
                int mid = menuObjects.get(m);
                int pos = m.getSectionPosition(s);
                int sid = writeNewSection(mid, pos, s);
                sectionObjects.put(s, sid);
                idToSectionObject.put(sid, s);
            }

            @Override
            public void notifyItemAdded(Menu m, Section s, MenuItem it) {
                int mid = menuObjects.get(m);
                int sid, pos;
                if (s != null) {
                    sid = sectionObjects.get(s);
                    pos = s.getItemPosition(it);
                } else {
                    sid = 0;
                    pos = m.getItemPosition(it);
                }
                int iid = writeNewItem(mid, sid, pos, it);
                itemObjects.put(it, iid);
                idToItemObject.put(iid, it);
            }

            @Override
            public void notifyMenuPublished(Menu m) {
                writeMenuChanges(m);

            }

            @Override
            public void notifyMenuDeleted(Menu m) {
                removeMenu(m);
            }

            @Override
            public void notifySectionRemoved(Menu m, Section s) {
                removeSection(s);
            }


            @Override
            public void notifySectionNameChanged(Menu m, Section s) {
                int mid = menuObjects.get(m);
                int pos = m.getSectionPosition(s);
                writeSectionChanges(mid, pos, s);
            }

            @Override
            public void notifySectionsRearranged(Menu m) {
                List<Section> sects = m.getSections();
                int mid = menuObjects.get(m);
                for (int i = 0; i < sects.size(); i++) {
                    writeSectionChanges(mid, i, sects.get(i));
                }

            }

            @Override
            public void notifyItemsRearranged(Menu m, Section s) {
                List<MenuItem> its = s.getItems();
                int mid = menuObjects.get(m);
                int sid = sectionObjects.get(s);
                for (int i = 0; i < its.size(); i++) {
                    writeItemChanges(mid, sid, i, its.get(i));
                }
            }

            @Override
            public void notifyItemsRearrangedInMenu(Menu m) {
                List<MenuItem> its = m.getItemsWithoutSection();
                int mid = menuObjects.get(m);
                for (int i = 0; i < its.size(); i++) {
                    writeItemChanges(mid, 0, i, its.get(i));
                }

            }

            @Override
            public void notifyItemMoved(Menu m, Section oldS, Section newS, MenuItem it) {
                int mid = menuObjects.get(m);
                int sid = (newS == null ? 0 : sectionObjects.get(newS));
                int itpos = (newS == null ? m.getItemPosition(it) : newS.getItemPosition(it));
                writeItemChanges(mid, sid, itpos, it);
            }

            @Override
            public void notifyItemDescriptionChanged(Menu m, MenuItem it) {
                int mid = menuObjects.get(m);
                Section s = m.getSection(it);
                int sid = (s == null ? 0 : sectionObjects.get(s));
                int itpos = (s == null ? m.getItemPosition(it) : s.getItemPosition(it));
                writeItemChanges(mid, sid, itpos, it);
            }

            @Override
            public void notifyItemDeleted(Menu m, MenuItem it) {
                removeItem(it);
            }

            @Override
            public void notifyMenuTitleChanged(Menu m) {
                writeMenuChanges(m);
            }
        });

        //todo implement receivers
        CateringAppManager.eventManager.addReceiver(new CatEventReceiver() {
            @Override
            public void notifyTaskAdded(Task task) {
//                System.out.println("DataManager() Notify" + task.getRecipe());
            }

            @Override
            public void notifyTaskRemoved(Task task) {
                //todo
                String SQL1 = "delete from assignment where assignment.task=?;\n";
                String SQL2 = "delete  from task where task.id=?;\n";
                try (PreparedStatement st1 = connection.prepareStatement(SQL1);
                     PreparedStatement st2 = connection.prepareStatement(SQL2)) {
                    st1.setInt(1, task.getId());
                    st2.setInt(1, task.getId());
                    int i1 = st1.executeUpdate();
                    int i2 = st2.executeUpdate();
                    System.out.println("[DB delete task] status:\n\t assignment =" + i1 + "; task= " + i2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyTaskSorted(Task task) {

            }

            @Override
            public void notifyTaskAssigned(Task task) {
                updateTask(task);
                if (task.getShift() != null || task.getCook() != null)
                    assignTask(task);
            }

            @Override
            public void notifyTaskAssignmentDeleted(Task task, User cook) {

            }

            @Override
            public void notifyEventSelected(CatEvent event) {

            }
        });
    }

    private int writeNewMenu(Menu m) {

        String sql = "INSERT INTO Menus(title, menuowner, published, fingerFood, " +
                "cookRequired, hotDishes, kitchenRequired, buffet) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        int id = -1;
        PreparedStatement pstmt = null;
        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, m.getTitle());
            pstmt.setInt(2, this.userObjects.get(m.getOwner()));
            pstmt.setBoolean(3, m.isPublished());
            pstmt.setBoolean(4, m.isFingerFood());
            pstmt.setBoolean(5, m.isCookRequired());
            pstmt.setBoolean(6, m.isHotDishes());
            pstmt.setBoolean(7, m.isKitchenRequired());
            pstmt.setBoolean(8, m.isBuffet());

            int r = pstmt.executeUpdate();

            if (r == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return id;
    }

    private void writeMenuChanges(Menu m) {
        int mid = menuObjects.get(m);
        int uid = userObjects.get(m.getOwner());
        String sql = "UPDATE Menus SET menuowner=?, published=?, fingerFood=?, cookRequired=?, hotDishes=?, "
                + "kitchenRequired=?, buffet=?, title=? WHERE id=" + mid;
        PreparedStatement pstmt = null;

        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, uid);
            pstmt.setBoolean(2, m.isPublished());
            pstmt.setBoolean(3, m.isFingerFood());
            pstmt.setBoolean(4, m.isCookRequired());
            pstmt.setBoolean(5, m.isHotDishes());
            pstmt.setBoolean(6, m.isKitchenRequired());
            pstmt.setBoolean(7, m.isBuffet());
            pstmt.setString(8, m.getTitle());

            int r = pstmt.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    private void removeMenu(Menu m) {
        int mId = menuObjects.get(m);
        String sqlItems = "DELETE FROM MenuItems WHERE menu=?";
        String sqlSections = "DELETE FROM Sections WHERE menu=?";
        String sqlMenu = "DELETE FROM Menus WHERE id=?";
        PreparedStatement pstItems = null;
        PreparedStatement pstSections = null;
        PreparedStatement pstMenu = null;
        try {
            connection.setAutoCommit(false);
            pstItems = connection.prepareStatement(sqlItems);
            pstSections = connection.prepareStatement(sqlSections);
            pstMenu = connection.prepareStatement(sqlMenu);
            pstItems.setInt(1, mId);
            pstSections.setInt(1, mId);
            pstMenu.setInt(1, mId);
            pstItems.executeUpdate();
            pstSections.executeUpdate();
            pstMenu.executeUpdate();
            connection.commit();
        } catch (SQLException exc) {
            exc.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                if (pstItems != null) pstItems.close();
                if (pstSections != null) pstSections.close();
                if (pstMenu != null) pstMenu.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }

        }
    }


    private int writeNewSection(int menuId, int position, Section sec) {

        String sql = "INSERT INTO Sections(menu, name, position) VALUES(?,?,?)";
        int id = -1;
        PreparedStatement pstmt = null;
        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setString(2, sec.getName());
            pstmt.setInt(3, position);

            int r = pstmt.executeUpdate();

            if (r == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

        return id;
    }

    private void writeSectionChanges(int menuId, int position, Section sec) {
        int sId = sectionObjects.get(sec);
        String sql = "UPDATE Sections SET menu=?, name=?, position=? WHERE id=" + sId;
        PreparedStatement pstmt = null;
        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setString(2, sec.getName());
            pstmt.setInt(3, position);

            pstmt.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    private void removeSection(Section s) {
        int sId = sectionObjects.get(s);
        String sqlItems = "DELETE FROM MenuItems WHERE section=?";
        String sqlSection = "DELETE FROM Sections WHERE id=?";
        PreparedStatement pstItems = null;
        PreparedStatement pstSection = null;
        try {
            connection.setAutoCommit(false);
            pstItems = connection.prepareStatement(sqlItems);
            pstSection = connection.prepareStatement(sqlSection);
            pstItems.setInt(1, sId);
            pstSection.setInt(1, sId);
            pstItems.executeUpdate();
            pstSection.executeUpdate();
            connection.commit();
        } catch (SQLException exc) {
            exc.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                if (pstItems != null) pstItems.close();
                if (pstSection != null) pstSection.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }

        }
    }

    private int writeNewItem(int menuId, int secId, int position, MenuItem item) {

        String sql = "INSERT INTO MenuItems(menu, section, description, recipe, position) " +
                "VALUES(?,?,?,?,?)";
        int id = -1;
        PreparedStatement pstmt = null;

        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setInt(2, secId);
            pstmt.setString(3, item.getDescription());
            pstmt.setInt(4, recipeObjects.get(item.getRecipe()));
            pstmt.setInt(5, position);

            int r = pstmt.executeUpdate();

            if (r == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

        return id;
    }

    private void writeItemChanges(int menuId, int secId, int position, MenuItem item) {
        int itId = itemObjects.get(item);
        String sql = "UPDATE MenuItems SET menu=?, section=?, description=?, recipe=?, position=? WHERE " +
                "id=" + itId;
        PreparedStatement pstmt = null;

        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setInt(2, secId);
            pstmt.setString(3, item.getDescription());
            pstmt.setInt(4, recipeObjects.get(item.getRecipe()));
            pstmt.setInt(5, position);

            pstmt.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    private void removeItem(MenuItem it) {
        int iId = itemObjects.get(it);
        String sqlItems = "DELETE FROM MenuItems WHERE id=?";
        PreparedStatement pstItems = null;
        try {
            pstItems = connection.prepareStatement(sqlItems);
            pstItems.setInt(1, iId);
            pstItems.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();

        } finally {
            try {
                if (pstItems != null) pstItems.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }

        }
    }

    public User loadUser(String userName) {
        PreparedStatement pst = null;
        String sql = "SELECT Users.id, Users.name, UserRoles.role FROM Users LEFT JOIN UserRoles on Users.id = "
                + "UserRoles.user where Users.name=?";
        User u = null;

        try {
            pst = this.connection.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (u == null) {
                    u = new User(userName);
                    int id = rs.getInt("id");
                    this.userObjects.put(u, id);
                    this.idToUserObject.put(id, u);
                }

                addUserRole(u, rs);

            }
            pst.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return u;
    }

    public List<Recipe> loadRecipes() {
        Statement st = null;
        String query = "SELECT * FROM Recipes";
        List<Recipe> ret = new ArrayList<>();

        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                char type = rs.getString("type").charAt(0);
                int id = rs.getInt("id");

                // Verifica se per caso l'ha già caricata
                Recipe rec = this.idToRecipeObject.get(id);

                if (rec == null) {
                    rec = createRecipeWithType(id, name, type);

                    if (rec != null) {
                        ret.add(rec);
                        this.recipeObjects.put(rec, id);
                        this.idToRecipeObject.put(id, rec);
                    }
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return ret;
    }

    public List<Menu> loadMenus() {
        List<Menu> ret = new ArrayList<>();
        Statement st = null;
        String query = "SELECT * FROM Menus";

        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");

                // Verifica se per caso l'ha già caricato
                Menu m = this.idToMenuObject.get(id);
                if (m == null) {

                    String title = rs.getString("title");

                    int ownerid = rs.getInt("menuowner");
                    User owner = this.innerLoadUser(ownerid);

                    m = new Menu(owner, title);
                    m.setPublished(rs.getBoolean("published"));
                    m.setBuffet(rs.getBoolean("buffet"));
                    m.setCookRequired(rs.getBoolean("cookRequired"));
                    m.setFingerFood(rs.getBoolean("fingerFood"));
                    m.setHotDishes(rs.getBoolean("hotDishes"));
                    m.setKitchenRequired(rs.getBoolean("kitchenRequired"));

                    // per sapere se il menu è in uso consulto la tabella degli eventi
                    // NdR: un menu è in uso anche se l'evento che lo usa è concluso o annullato
                    Statement st2 = this.connection.createStatement();
                    String query2 = "SELECT Events.id FROM Events JOIN Menus M on Events.menu = M.id WHERE M.id=" + id;
                    ResultSet rs2 = st2.executeQuery(query2);
                    m.setInUse(rs2.next());
                    st2.close();
                    loadMenuSections(id, m);
                    loadMenuItems(id, m);


                    ret.add(m);
                    this.menuObjects.put(m, id);
                    this.idToMenuObject.put(id, m);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return ret;
    }

    private void loadMenuItems(int id, Menu m) {
        // Caricamento voci
        // Non verifichiamo se un MenuItem è già stato creato perché
        // questo può avvenire solo nel contesto del caricamento di un Menu
        // e il MenuItem può essere già creato solo se il Menu è stato creato;
        // il controllo sul Menu avviene già in loadMenus
        Statement st = null;
        String query = "SELECT MenuItems.* FROM MenuItems WHERE MenuItems.menu=" + id
                + " ORDER BY MenuItems.position";
        try {
            st = this.connection.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String description = rs.getString("description");
                int idSec = rs.getInt("section");
                int idIt = rs.getInt("id");
                int idRec = rs.getInt("recipe");

                Recipe rec = this.innerLoadRecipe(idRec);

                Section sec = null;
                if (idSec > 0) {
                    // la sezione a questo punto dovrebbe essere già stata aggiunta
                    sec = this.idToSectionObject.get(idSec);
                }
                MenuItem it = m.addItem(rec, sec, description);
                this.itemObjects.put(it, idIt);
                this.idToItemObject.put(idIt, it);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    private Recipe innerLoadRecipe(int idRec) {
        // verifico se l'ho già caricato in precedenza
        Recipe rec = this.idToRecipeObject.get(idRec);
        if (rec != null) return rec;

        Statement st = null;

        String query = "SELECT * FROM Recipes WHERE Recipes.id = " + idRec;
        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");
                char type = rs.getString("type").charAt(0);
                rec = createRecipeWithType(id, name, type);
                this.recipeObjects.put(rec, idRec);
                this.idToRecipeObject.put(idRec, rec);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

        return rec;
    }

    private Recipe createRecipeWithType(int id, String name, char type) {
        switch (type) {
            case 'r':
                return new Recipe(id, name, Recipe.Type.Dish);
            case 'p':
                return new Recipe(id, name, Recipe.Type.Preparation);

        }
        return null;
    }

    private void loadMenuSections(int id, Menu m) {
        // Caricamento sezioni
        // Non verifichiamo se una Section è già stata creata perché
        // questo può avvenire solo nel contesto del caricamento di un Menu
        // e la Section può essere già creata solo se il Menu è stato creato;
        // il controllo sul Menu avviene già in loadMenus
        Statement st = null;
        String query = "SELECT Sections.* FROM Sections WHERE Sections.menu=" + id + " ORDER BY Sections.position";

        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                int idSec = rs.getInt("id");

                Section sec = m.addSection(name);
                this.sectionObjects.put(sec, idSec);
                this.idToSectionObject.put(idSec, sec);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

    }

    private User innerLoadUser(int userId) {
        // verifico se l'ho già caricato in precedenza
        User u = this.idToUserObject.get(userId);
        if (u != null) return u;

        Statement st = null;
        String query = "SELECT Users.id, Users.name, UserRoles.role FROM Users LEFT JOIN UserRoles on Users.id = " +
                "UserRoles.user where Users.id=" + userId;
        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (u == null) {
                    u = new User(rs.getString("name"));
                    this.userObjects.put(u, userId);
                    this.idToUserObject.put(userId, u);
                }
                addUserRole(u, rs);
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

        return u;
    }

    private void addUserRole(User u, ResultSet rs) throws SQLException {
        char roleName = rs.getString("role").charAt(0);
        switch (roleName) {
            case 'c':
                u.addRole(User.Role.Cuoco);
                break;
            case 'h':
                u.addRole(User.Role.Chef);
                break;
            case 'o':
                u.addRole(User.Role.Organizzatore);
                break;
            case 's':
                u.addRole(User.Role.Servizio);
                break;
        }
    }

    public List<CatEvent> loadEvents(User chef) {
        String SQL = "SELECT ev.id, ev.nome, ev.menu\n" +
                "FROM event_chef ec,\n" +
                "     events ev,\n" +
                "     users\n" +
                "WHERE users.name = ?\n" +
                "    AND users.id = ec.chef\n" +
                "    AND ec.event = ev.id;";

        PreparedStatement st = null;
        List<CatEvent> allEvents = new ArrayList<>();

        try {
            st = this.connection.prepareStatement(SQL);
            st.setString(1, chef.getName());

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int menu = rs.getInt("menu");
                allEvents.add(new CatEvent(id, nome, menu, chef));
                //todo caricare tutte le task di currentEvent
                //SELECT * from
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return allEvents;
    }

    public List<Task> loadTasks(CatEvent event) {
        loadShifts();
        loadUsers();
        List<Recipe> recipes = loadRecipes(); //non rimuovere
        List<Task> ret = new ArrayList<>();
        PreparedStatement st = null;

        String SQL = "SELECT t.id task_id, t.index position, duration, is_assigned, is_completed, difficulty, quantity, " +
                "r.id recipe_id, a.shift shift, a.user cook " +
                "FROM task t JOIN recipes r ON t.recipe=r.id LEFT JOIN assignment a " +
                "ON t.id = a.task WHERE t.event = ?;";

        try {
            st = this.connection.prepareStatement(SQL);
            st.setInt(1, event.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                int shiftId = rs.getInt("shift");
                int cookId = rs.getInt("cook");
                boolean isAssigned = rs.getBoolean("is_assigned");
                boolean isCompleted = rs.getBoolean("is_completed");
                int difficulty = rs.getInt("difficulty");
                int duration = rs.getInt("duration");
                int quantity = rs.getInt("quantity");
                int index = rs.getInt("position");
                int recipeId = rs.getInt("recipe_id");

                Task task = new Task(taskId,
                        this.idToRecipeObject.get(recipeId),
                        this.idToShiftObject.get(shiftId),
                        this.idToUserObject.get(cookId),
                        quantity,
                        difficulty,
                        isCompleted,
                        isAssigned,
                        duration,
                        index);
                ret.add(task);
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

        ret.sort(Comparator.comparing(Task::getIndex).reversed());
        return ret;
    }

    public List<Shift> getShifts() {
        List<Shift> ret = new ArrayList<>();
        PreparedStatement st = null;


        String SQL = "select * FROM shift";
        try {
            st = this.connection.prepareStatement(SQL);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Date date = rs.getDate("date");
                String type = rs.getString("type");

                Shift shift = new Shift(id, date, type);

                ret.add(shift);
                shiftObjects.put(shift, id);
                idToShiftObject.put(id, shift);

            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return ret;
    }

    public void loadShifts() {
        PreparedStatement st = null;


        String SQL = "select * FROM shift";
        try {
            st = this.connection.prepareStatement(SQL);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Date date = rs.getDate("date");
                String type = rs.getString("type");

                Shift shift = new Shift(id, date, type);

                shiftObjects.put(shift, id);
                idToShiftObject.put(id, shift);

            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    public void loadUsers() {
        PreparedStatement st = null;


        String SQL = "select * FROM users";
        try {
            st = this.connection.prepareStatement(SQL);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                User cook = new User(id, name);

                userObjects.put(cook, id);
                idToUserObject.put(id, cook);

            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    public List<User> loadUsersInShift(Shift shift) {
        List<User> ret = new ArrayList<>();
        PreparedStatement st = null;

        String SQL = "select * FROM users u " +
                "JOIN shift_user a ON u.id = a.user " +
                "WHERE shift = ?";
        try {
            st = this.connection.prepareStatement(SQL);
            st.setInt(1, shift.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                User user = new User(id, name);

                ret.add(user);

            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return ret;
    }

    private void updateTask(Task task) {
        PreparedStatement st = null;
        String SQL;
        if (task.getShift() != null && task.getCook() != null)
            SQL = "UPDATE task SET quantity = ?, duration = ?, difficulty = ?, is_assigned = 1 WHERE id = ?";
        else
            SQL = "UPDATE task SET quantity = ?, duration = ?, difficulty = ? WHERE id = ?";

        try {
            st = this.connection.prepareStatement(SQL);
            st.setInt(1, task.getQuantity());
            st.setInt(2, task.getDurationMinutes());
            st.setInt(3, task.getDifficulty());
            st.setInt(4, task.getId());
            System.out.println(st.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    public int addTask(Recipe recipe) {
        String SQL = "INSERT into task (recipe, event, is_assigned, is_completed, `index`, quantity, difficulty, duration) " +
                "values (?, ?, 0, 0, 0, 0, 0, 0);";

        String getId = "SELECT LAST_INSERT_ID() AS last_id FROM task;";

        PreparedStatement st = null;
        try {
            st = this.connection.prepareStatement(SQL);
            st.setInt(1, recipe.getId());
            int eventId = CateringAppManager.eventManager.getCurrentEvent().getId();
            st.setInt(2, eventId);
            System.out.println("Exec query :\n" + st.toString());
            st.execute();

            st = this.connection.prepareStatement(getId);
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt("last_id");


        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return 1;
    }

    private void assignTask(Task task) {
        PreparedStatement st_check = null;
        PreparedStatement st_update = null;
        PreparedStatement st_insert = null;

        String check = "SELECT id FROM assignment WHERE task = ?";
        String update_all = "UPDATE assignment SET shift = ?, user = ? WHERE task = ?";
        String update_cook = "UPDATE assignment SET user = ? WHERE task = ?";
        String update_shift = "UPDATE assignment SET shift = ? WHERE task = ?";
        String insert_all = "INSERT INTO assignment(shift, user, task) VALUES(?, ?, ?)";
        String insert_cook = "INSERT INTO assignment(user, task) VALUES(?, ?)";
        String insert_shift = "INSERT INTO assignment(shift, task) VALUES(?, ?)";

        try {
            st_check = this.connection.prepareStatement(check);
            st_check.setInt(1, task.getId());
            ResultSet rs = st_check.executeQuery();
            if (rs.next()) { //assegnamento della task esiste già, lo modifico
                if (task.getShift() != null && task.getCook() != null) {
                    st_update = this.connection.prepareStatement(update_all);
                    st_update.setInt(1, task.getShift().getId());
                    st_update.setInt(2, task.getCook().getId());
                    st_update.setInt(3, task.getId());
                    st_update.executeUpdate();
                } else if (task.getShift() != null) {
                    st_update = this.connection.prepareStatement(update_shift);
                    st_update.setInt(1, task.getShift().getId());
                    st_update.setInt(2, task.getId());
                    st_update.executeUpdate();
                } else {
                    st_update = this.connection.prepareStatement(update_cook);
                    st_update.setInt(1, task.getCook().getId());
                    st_update.setInt(2, task.getId());
                    st_update.executeUpdate();
                }
            } else { //assegnamento della task non esisteva, inserisco nuova tupla
                if (task.getShift() != null && task.getCook() != null) {
                    st_insert = this.connection.prepareStatement(insert_all);
                    st_insert.setInt(1, task.getShift().getId());
                    st_insert.setInt(2, task.getCook().getId());
                    st_insert.setInt(3, task.getId());
                    System.out.println("shift: " + task.getShift().getId() +
                            "cook: " + task.getCook().getId() +
                            "task: " + task.getId());
                    st_insert.execute();
                } else if (task.getShift() != null) {
                    st_insert = this.connection.prepareStatement(insert_shift);
                    st_insert.setInt(1, task.getShift().getId());
                    st_insert.setInt(2, task.getId());
                    st_insert.execute();
                } else {
                    st_insert = this.connection.prepareStatement(insert_cook);
                    st_insert.setInt(1, task.getCook().getId());
                    st_insert.setInt(2, task.getId());
                    st_insert.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st_update != null) st_update.close();
                if (st_check != null) st_check.close();
                if (st_insert != null) st_insert.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }

    public void sortTask(Task task, int index) {
        String SQL = "UPDATE task\n" +
                "SET `index` = ?\n" +
                "WHERE id = ?";
        PreparedStatement st;
        try {
            st = connection.prepareStatement(SQL);
            st.setInt(1, index);
            st.setInt(2, task.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
