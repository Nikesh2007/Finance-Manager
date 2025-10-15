/*
 * ModernFinanceApp.java - ULTRA-MODERN FINANCE MANAGER WITH STUNNING ANIMATIONS
 * Features: Glassmorphism UI, Particle Effects, Smooth Transitions, Modern Cards
 * FIXED: Quick Action buttons now work properly
 * IMPROVED: Quick transaction dialogs now have enhanced black theme with white text
 */

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ModernFinanceApp extends Application {
    
    private static final String BASE_FOLDER = System.getProperty("user.home") + "/Desktop/app";
    private static final String ACCOUNTS_FILE = BASE_FOLDER + "/Accounts.csv";
    
    private Stage primaryStage;
    private String currentUserId = null;
    private VBox transactionsList;
    private String currentTab = "Dashboard";
    private StackPane mainContentArea;
    private BorderPane rootPane;

    // Enhanced Data Model
    public static class Transaction {
        private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
        private final StringProperty type = new SimpleStringProperty();
        private final StringProperty category = new SimpleStringProperty();
        private final DoubleProperty amount = new SimpleDoubleProperty();
        private final StringProperty note = new SimpleStringProperty();

        public Transaction(LocalDate date, String type, String category, double amount, String note) {
            this.date.set(date);
            this.type.set(type);
            this.category.set(category);
            this.amount.set(amount);
            this.note.set(note);
        }

        // Getters
        public LocalDate getDate() { return date.get(); }
        public String getType() { return type.get(); }
        public String getCategory() { return category.get(); }
        public double getAmount() { return amount.get(); }
        public String getNote() { return note.get(); }
    }

    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private final ObservableList<String> categories = FXCollections.observableArrayList(
            "Food & Dining", "Transportation", "Shopping", "Entertainment", "Bills & Utilities", 
            "Healthcare", "Education", "Investment", "Travel", "Income", "Other"
    );

    // UI Components
    private Label incomeLabel, expenseLabel, balanceLabel;
    private PieChart expenseChart;
    private AreaChart<String, Number> trendChart;
    private double monthlyBudget = 50000;
    private Timeline backgroundAnimation;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Modern Finance Manager");
        primaryStage.setMaximized(true);
        
        initializeDirectories();
        showLoginScreen();
    }

    private void initializeDirectories() {
        try {
            java.nio.file.Path appFolder = Paths.get(BASE_FOLDER);
            if (!Files.exists(appFolder)) {
                Files.createDirectories(appFolder);
            }
            
            java.nio.file.Path accountsFile = Paths.get(ACCOUNTS_FILE);
            if (!Files.exists(accountsFile)) {
                Files.write(accountsFile, Arrays.asList("username,password,email,registration_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // STUNNING LOGIN SCREEN WITH GLASSMORPHISM
    private void showLoginScreen() {
        rootPane = new BorderPane();
        
        // Create animated particle background
        Pane particleLayer = createParticleBackground();
        
        // Main login container with glassmorphism effect
        VBox loginContainer = createGlassmorphismContainer();
        
        // Animated title with glow effect
        VBox titleSection = createAnimatedTitle();
        
        // Login form with smooth animations
        VBox loginForm = createModernLoginForm();
        VBox registerForm = createModernRegisterForm();
        registerForm.setOpacity(0);
        registerForm.setVisible(false);
        
        StackPane formContainer = new StackPane(loginForm, registerForm);
        
        loginContainer.getChildren().addAll(titleSection, formContainer);
        
        // Center everything with floating effect
        StackPane centerStack = new StackPane(loginContainer);
        centerStack.setAlignment(Pos.CENTER);
        
        // Layer everything
        StackPane fullStack = new StackPane(particleLayer, centerStack);
        rootPane.setCenter(fullStack);
        
        // Animated gradient background
        rootPane.setBackground(createAnimatedBackground());
        
        Scene scene = new Scene(rootPane, 1400, 900);
        scene.getStylesheets().add(createModernCSS());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        setupFormAnimations(loginForm, registerForm);
        startBackgroundAnimation();
        
        // Entrance animation
        loginContainer.setTranslateY(100);
        loginContainer.setOpacity(0);
        
        Timeline entrance = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(loginContainer.translateYProperty(), 100, Interpolator.EASE_OUT),
                new KeyValue(loginContainer.opacityProperty(), 0)),
            new KeyFrame(Duration.seconds(1), 
                new KeyValue(loginContainer.translateYProperty(), 0, Interpolator.EASE_OUT),
                new KeyValue(loginContainer.opacityProperty(), 1))
        );
        entrance.play();
    }

    private Pane createParticleBackground() {
        Pane particlePane = new Pane();
        particlePane.setMouseTransparent(true);
        
        // Create floating particles
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            Circle particle = new Circle(random.nextDouble() * 3 + 1);
            particle.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.1 + random.nextDouble() * 0.2));
            particle.setCenterX(random.nextDouble() * 1400);
            particle.setCenterY(random.nextDouble() * 900);
            
            // Floating animation
            Timeline floatAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(particle.translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(10 + random.nextDouble() * 10), 
                    new KeyValue(particle.translateYProperty(), -50 - random.nextDouble() * 100))
            );
            floatAnimation.setCycleCount(Timeline.INDEFINITE);
            floatAnimation.setAutoReverse(true);
            floatAnimation.play();
            
            particlePane.getChildren().add(particle);
        }
        
        return particlePane;
    }

    private VBox createGlassmorphismContainer() {
        VBox container = new VBox(40);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(60));
        container.setMaxWidth(500);
        container.setMaxHeight(700);
        
        // Glassmorphism effect
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                          "-fx-background-radius: 30; " +
                          "-fx-border-radius: 30; " +
                          "-fx-border-color: rgba(255, 255, 255, 0.2); " +
                          "-fx-border-width: 1;");
        
        // Add blur effect
        GaussianBlur blur = new GaussianBlur(10);
        container.setEffect(blur);
        
        // Drop shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK.deriveColor(0, 1, 1, 0.3));
        shadow.setRadius(20);
        shadow.setOffsetY(10);
        
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.WHITE.deriveColor(0, 1, 1, 0.1));
        innerShadow.setRadius(5);
        
        shadow.setInput(innerShadow);
        container.setEffect(shadow);
        
        return container;
    }

    private VBox createAnimatedTitle() {
        VBox titleSection = new VBox(20);
        titleSection.setAlignment(Pos.CENTER);
        
        // Main title with gradient text effect
        Text title = new Text("FinanceFlow");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 48));
        title.setFill(createTextGradient());
        
        // Glow effect
        Glow glow = new Glow(0.8);
        title.setEffect(glow);
        
        Text subtitle = new Text("Your Smart Financial Companion");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitle.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.8));
        
        // Connection status with pulse animation
        HBox statusBox = new HBox(8);
        statusBox.setAlignment(Pos.CENTER);
        
        Circle statusDot = new Circle(4, Color.LIGHTGREEN);
        Timeline pulse = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(statusDot.radiusProperty(), 4)),
            new KeyFrame(Duration.seconds(1), new KeyValue(statusDot.radiusProperty(), 6))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();
        
        Text statusText = new Text("Connected to Local Storage");
        statusText.setFont(Font.font("Segoe UI", 12));
        statusText.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
        
        statusBox.getChildren().addAll(statusDot, statusText);
        titleSection.getChildren().addAll(title, subtitle, statusBox);
        
        return titleSection;
    }

    private VBox createModernLoginForm() {
        VBox form = new VBox(25);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        
        Text formTitle = new Text("Welcome Back");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        formTitle.setFill(Color.WHITE);
        
        VBox fields = new VBox(20);
        fields.setAlignment(Pos.CENTER);
        
        // Modern text fields with floating labels
        VBox userFieldContainer = createFloatingLabelField("Username", false);
        TextField userField = (TextField) userFieldContainer.getChildren().get(1);
        
        VBox passFieldContainer = createFloatingLabelField("Password", true);
        PasswordField passField = (PasswordField) passFieldContainer.getChildren().get(1);
        
        fields.getChildren().addAll(userFieldContainer, passFieldContainer);
        
        // Modern buttons with hover animations
        Button loginBtn = createModernButton("Login", "#6366f1", true);
        Button registerBtn = createModernButton("Create Account", "transparent", false);
        
        VBox buttons = new VBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(loginBtn, registerBtn);
        
        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font("Segoe UI", 12));
        messageLabel.setVisible(false);
        
        form.getChildren().addAll(formTitle, fields, buttons, messageLabel);
        
        // Store components for later use
        form.setUserData(new Object[]{userField, passField, loginBtn, registerBtn, messageLabel});
        
        return form;
    }

    private VBox createModernRegisterForm() {
        VBox form = new VBox(25);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        
        Text formTitle = new Text("Join FinanceFlow");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        formTitle.setFill(Color.WHITE);
        
        VBox fields = new VBox(20);
        fields.setAlignment(Pos.CENTER);
        
        VBox userFieldContainer = createFloatingLabelField("Choose Username", false);
        TextField userField = (TextField) userFieldContainer.getChildren().get(1);
        
        VBox passFieldContainer = createFloatingLabelField("Password", true);
        PasswordField passField = (PasswordField) passFieldContainer.getChildren().get(1);
        
        VBox confirmFieldContainer = createFloatingLabelField("Confirm Password", true);
        PasswordField confirmField = (PasswordField) confirmFieldContainer.getChildren().get(1);
        
        fields.getChildren().addAll(userFieldContainer, passFieldContainer, confirmFieldContainer);
        
        Button createBtn = createModernButton("Create Account", "#10b981", true);
        Button backBtn = createModernButton("Back to Login", "transparent", false);
        
        VBox buttons = new VBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(createBtn, backBtn);
        
        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font("Segoe UI", 12));
        messageLabel.setVisible(false);
        
        form.getChildren().addAll(formTitle, fields, buttons, messageLabel);
        form.setUserData(new Object[]{userField, passField, confirmField, createBtn, backBtn, messageLabel});
        
        return form;
    }

    private VBox createFloatingLabelField(String labelText, boolean isPassword) {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER_LEFT);
        
        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE.deriveColor(0, 1, 1, 0.8));
        label.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 14));
        
        TextField field = isPassword ? new PasswordField() : new TextField();
        field.setPrefWidth(350);
        field.setPrefHeight(50);
        field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                      "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                      "-fx-border-radius: 15; " +
                      "-fx-background-radius: 15; " +
                      "-fx-padding: 15; " +
                      "-fx-text-fill: white; " +
                      "-fx-font-size: 16px; " +
                      "-fx-prompt-text-fill: rgba(255, 255, 255, 0.5);");
        
        // Focus animations
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            Timeline focusAnimation = new Timeline();
            if (newVal) {
                focusAnimation.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, 
                        new KeyValue(field.scaleXProperty(), 1),
                        new KeyValue(field.scaleYProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.2), 
                        new KeyValue(field.scaleXProperty(), 1.02),
                        new KeyValue(field.scaleYProperty(), 1.02))
                );
                field.setStyle(field.getStyle() + "; -fx-border-color: #6366f1; -fx-border-width: 2;");
            } else {
                focusAnimation.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, 
                        new KeyValue(field.scaleXProperty(), 1.02),
                        new KeyValue(field.scaleYProperty(), 1.02)),
                    new KeyFrame(Duration.seconds(0.2), 
                        new KeyValue(field.scaleXProperty(), 1),
                        new KeyValue(field.scaleYProperty(), 1))
                );
                field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                              "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                              "-fx-border-radius: 15; " +
                              "-fx-background-radius: 15; " +
                              "-fx-padding: 15; " +
                              "-fx-text-fill: white; " +
                              "-fx-font-size: 16px; " +
                              "-fx-prompt-text-fill: rgba(255, 255, 255, 0.5);");
            }
            focusAnimation.play();
        });
        
        container.getChildren().addAll(label, field);
        return container;
    }

    private Button createModernButton(String text, String baseColor, boolean isPrimary) {
        Button button = new Button(text);
        button.setPrefWidth(350);
        button.setPrefHeight(50);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        
        if (isPrimary) {
            button.setStyle("-fx-background-color: " + baseColor + "; " +
                           "-fx-text-fill: white; " +
                           "-fx-background-radius: 25; " +
                           "-fx-cursor: hand;");
        } else {
            button.setStyle("-fx-background-color: transparent; " +
                           "-fx-text-fill: white; " +
                           "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                           "-fx-border-width: 2; " +
                           "-fx-border-radius: 25; " +
                           "-fx-background-radius: 25; " +
                           "-fx-cursor: hand;");
        }
        
        // Hover and click animations
        button.setOnMouseEntered(e -> {
            Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 1)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(button.scaleXProperty(), 1.05))
            );
            hoverIn.play();
        });
        
        button.setOnMouseExited(e -> {
            Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 1.05)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(button.scaleXProperty(), 1))
            );
            hoverOut.play();
        });
        
        return button;
    }

    private void setupFormAnimations(VBox loginForm, VBox registerForm) {
        Object[] loginComponents = (Object[]) loginForm.getUserData();
        TextField loginUserField = (TextField) loginComponents[0];
        PasswordField loginPassField = (PasswordField) loginComponents[1];
        Button loginBtn = (Button) loginComponents[2];
        Button showRegisterBtn = (Button) loginComponents[3];
        Label loginMessageLabel = (Label) loginComponents[4];
        
        Object[] registerComponents = (Object[]) registerForm.getUserData();
        TextField regUserField = (TextField) registerComponents[0];
        PasswordField regPassField = (PasswordField) registerComponents[1];
        PasswordField confirmPassField = (PasswordField) registerComponents[2];
        Button createBtn = (Button) registerComponents[3];
        Button backBtn = (Button) registerComponents[4];
        Label regMessageLabel = (Label) registerComponents[5];
        
        // Smooth form transitions
        showRegisterBtn.setOnAction(e -> {
            Timeline hideLogin = new Timeline(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(loginForm.opacityProperty(), 1),
                    new KeyValue(loginForm.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(0.3), 
                    new KeyValue(loginForm.opacityProperty(), 0),
                    new KeyValue(loginForm.translateXProperty(), -100))
            );
            
            hideLogin.setOnFinished(event -> {
                loginForm.setVisible(false);
                registerForm.setVisible(true);
                
                Timeline showRegister = new Timeline(
                    new KeyFrame(Duration.ZERO, 
                        new KeyValue(registerForm.opacityProperty(), 0),
                        new KeyValue(registerForm.translateXProperty(), 100)),
                    new KeyFrame(Duration.seconds(0.3), 
                        new KeyValue(registerForm.opacityProperty(), 1),
                        new KeyValue(registerForm.translateXProperty(), 0))
                );
                showRegister.play();
            });
            hideLogin.play();
        });
        
        backBtn.setOnAction(e -> {
            Timeline hideRegister = new Timeline(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(registerForm.opacityProperty(), 1),
                    new KeyValue(registerForm.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(0.3), 
                    new KeyValue(registerForm.opacityProperty(), 0),
                    new KeyValue(registerForm.translateXProperty(), 100))
            );
            
            hideRegister.setOnFinished(event -> {
                registerForm.setVisible(false);
                loginForm.setVisible(true);
                
                Timeline showLogin = new Timeline(
                    new KeyFrame(Duration.ZERO, 
                        new KeyValue(loginForm.opacityProperty(), 0),
                        new KeyValue(loginForm.translateXProperty(), -100)),
                    new KeyFrame(Duration.seconds(0.3), 
                        new KeyValue(loginForm.opacityProperty(), 1),
                        new KeyValue(loginForm.translateXProperty(), 0))
                );
                showLogin.play();
            });
            hideRegister.play();
        });
        
        // Authentication handlers
        loginBtn.setOnAction(e -> handleLogin(loginUserField.getText(), loginPassField.getText(), loginBtn, loginMessageLabel));
        createBtn.setOnAction(e -> handleRegister(regUserField.getText(), regPassField.getText(), confirmPassField.getText(), createBtn, regMessageLabel, loginForm, registerForm));
        
        Platform.runLater(() -> loginUserField.requestFocus());
    }

    // STUNNING MAIN APPLICATION
    private void showMainApp() {
        primaryStage.setTitle("FinanceFlow - " + currentUserId);
        
        rootPane = new BorderPane();
        
        // Create main layout with floating panels
        createMainLayout();
        
        Scene scene = new Scene(rootPane, 1600, 1000);
        scene.getStylesheets().add(createMainAppCSS());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        
        loadUserTransactions();
        
        // Spectacular entrance animation
        rootPane.setOpacity(0);
        Timeline entrance = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(rootPane.opacityProperty(), 0)),
            new KeyFrame(Duration.seconds(0.8), new KeyValue(rootPane.opacityProperty(), 1))
        );
        entrance.play();
    }

    private void createMainLayout() {
        // Animated gradient background
        rootPane.setBackground(createMainAppBackground());
        
        // Top navigation bar with glassmorphism
        HBox topNav = createTopNavigation();
        rootPane.setTop(topNav);
        
        // Sidebar with floating effect
        VBox sidebar = createModernSidebar();
        rootPane.setLeft(sidebar);
        
        // Main content area with cards
        mainContentArea = new StackPane();
        rootPane.setCenter(mainContentArea);
        
        // Show dashboard by default
        showDashboard();
        
        // Floating action button
        Button fab = createFloatingActionButton();
        StackPane fabContainer = new StackPane(fab);
        fabContainer.setAlignment(Pos.BOTTOM_RIGHT);
        fabContainer.setPadding(new Insets(30));
        fabContainer.setMouseTransparent(true);
        fab.setMouseTransparent(false);
        
        StackPane mainStack = new StackPane(mainContentArea, fabContainer);
        rootPane.setCenter(mainStack);
    }

    private HBox createTopNavigation() {
        HBox nav = new HBox(20);
        nav.setPadding(new Insets(20, 30, 20, 30));
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setPrefHeight(80);
        
        // Glassmorphism effect
        nav.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                     "-fx-backdrop-filter: blur(10px);");
        
        // App title
        Text appTitle = new Text("FinanceFlow");
        appTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        appTitle.setFill(createTextGradient());
        
        // Quick stats
        HBox statsBox = createQuickStats();
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // User profile
        HBox profileBox = createUserProfile();
        
        nav.getChildren().addAll(appTitle, statsBox, spacer, profileBox);
        
        return nav;
    }

    private HBox createQuickStats() {
        HBox stats = new HBox(30);
        stats.setAlignment(Pos.CENTER);
        
        incomeLabel = createStatCard("Income", "₹0", "#10b981");
        expenseLabel = createStatCard("Expenses", "₹0", "#ef4444");
        balanceLabel = createStatCard("Balance", "₹0", "#6366f1");
        
        stats.getChildren().addAll(incomeLabel, expenseLabel, balanceLabel);
        
        return stats;
    }

    private Label createStatCard(String title, String value, String color) {
        Label card = new Label(title + "\n" + value);
        card.setTextFill(Color.WHITE);
        card.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(120);
        card.setStyle("-fx-background-color: " + color + "; " +
                      "-fx-background-radius: 15; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        
        return card;
    }

    private HBox createUserProfile() {
        HBox profile = new HBox(15);
        profile.setAlignment(Pos.CENTER);
        
        // Avatar circle
        Circle avatar = new Circle(20);
        avatar.setFill(createTextGradient());
        
        VBox userInfo = new VBox(2);
        Text username = new Text(currentUserId);
        username.setFill(Color.WHITE);
        username.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Text status = new Text("Premium User");
        status.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
        status.setFont(Font.font("Segoe UI", 10));
        
        userInfo.getChildren().addAll(username, status);
        
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.8); " +
                          "-fx-text-fill: white; " +
                          "-fx-background-radius: 20; " +
                          "-fx-padding: 8 16; " +
                          "-fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            currentUserId = null;
            transactions.clear();
            showLoginScreen();
        });
        
        profile.getChildren().addAll(avatar, userInfo, logoutBtn);
        
        return profile;
    }

    private VBox createModernSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); " +
                        "-fx-backdrop-filter: blur(20px);");
        
        // Navigation buttons
        String[] navItems = {"Dashboard", "Transactions", "Analytics", "Budget", "Settings"};
        String[] navIcons = {"📊", "💳", "📈", "💰", "⚙️"};
        
        for (int i = 0; i < navItems.length; i++) {
            Button navBtn = createSidebarButton(navItems[i], navIcons[i]);
            final String tabName = navItems[i];
            navBtn.setOnAction(e -> switchTab(tabName));
            sidebar.getChildren().add(navBtn);
        }
        
        return sidebar;
    }

    private Button createSidebarButton(String text, String icon) {
        Button btn = new Button(icon + "  " + text);
        btn.setPrefWidth(240);
        btn.setPrefHeight(50);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 16));
        
        String baseStyle = "-fx-background-color: transparent; " +
                          "-fx-text-fill: white; " +
                          "-fx-background-radius: 15; " +
                          "-fx-cursor: hand; " +
                          "-fx-padding: 15;";
        
        String activeStyle = "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 15;";
        
        btn.setStyle(baseStyle);
        
        btn.setOnMouseEntered(e -> {
            if (!btn.getText().contains(currentTab)) {
                btn.setStyle(activeStyle);
            }
        });
        
        btn.setOnMouseExited(e -> {
            if (!btn.getText().contains(currentTab)) {
                btn.setStyle(baseStyle);
            }
        });
        
        return btn;
    }

    private Button createFloatingActionButton() {
        Button fab = new Button("+");
        fab.setPrefSize(70, 70);
        fab.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        fab.setTextFill(Color.WHITE);
        fab.setStyle("-fx-background-color: linear-gradient(45deg, #667eea, #764ba2); " +
                     "-fx-background-radius: 35; " +
                     "-fx-cursor: hand; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 8);");
        
        // Floating animation
        Timeline float_animation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(fab.translateYProperty(), 0)),
            new KeyFrame(Duration.seconds(2), new KeyValue(fab.translateYProperty(), -10))
        );
        float_animation.setCycleCount(Timeline.INDEFINITE);
        float_animation.setAutoReverse(true);
        float_animation.play();
        
        fab.setOnAction(e -> showAddTransactionDialog());
        
        return fab;
    }

    private void switchTab(String tabName) {
        currentTab = tabName;
        
        // Update sidebar button states
        VBox sidebar = (VBox) rootPane.getLeft();
        for (Node node : sidebar.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.getText().contains(tabName)) {
                    btn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 15;");
                } else {
                    btn.setStyle("-fx-background-color: transparent; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 15;");
                }
            }
        }
        
        // Show corresponding content
        switch (tabName) {
            case "Dashboard": showDashboard(); break;
            case "Transactions": showTransactions(); break;
            case "Analytics": showAnalytics(); break;
            case "Budget": showBudget(); break;
            case "Settings": showSettings(); break;
        }
    }

    private void showDashboard() {
        VBox dashboard = new VBox(30);
        dashboard.setPadding(new Insets(30));
        
        // Welcome section
        HBox welcomeSection = createWelcomeSection();
        
        // Charts section
        HBox chartsSection = createChartsSection();
        
        // Recent transactions
        VBox recentSection = createRecentTransactionsSection();
        
        dashboard.getChildren().addAll(welcomeSection, chartsSection, recentSection);
        
        ScrollPane scroll = new ScrollPane(dashboard);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        updateContentWithAnimation(scroll);
    }

    private HBox createWelcomeSection() {
        HBox section = new HBox(30);
        section.setAlignment(Pos.CENTER_LEFT);
        
        VBox welcomeText = new VBox(10);
        Text greeting = new Text("Welcome back, " + currentUserId + "!");
        greeting.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        greeting.setFill(Color.WHITE);
        
        Text subtitle = new Text("Here's your financial overview");
        subtitle.setFont(Font.font("Segoe UI", 18));
        subtitle.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.8));
        
        welcomeText.getChildren().addAll(greeting, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // FIXED: Quick action buttons with proper event handlers
        VBox quickActions = new VBox(10);
        Button quickExpenseBtn = createQuickActionButton("Quick Expense", "#ef4444");
        Button quickIncomeBtn = createQuickActionButton("Quick Income", "#10b981");
        
        // FIXED: Add click handlers to quick action buttons
        quickExpenseBtn.setOnAction(e -> showQuickTransactionDialog("Expense"));
        quickIncomeBtn.setOnAction(e -> showQuickTransactionDialog("Income"));
        
        quickActions.getChildren().addAll(quickExpenseBtn, quickIncomeBtn);
        
        section.getChildren().addAll(welcomeText, spacer, quickActions);
        
        return section;
    }

    private Button createQuickActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(150);
        btn.setPrefHeight(45);
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btn.setStyle("-fx-background-color: " + color + "; " +
                     "-fx-background-radius: 22; " +
                     "-fx-cursor: hand;");
        
        // FIXED: Add hover effects to make buttons more interactive
        btn.setOnMouseEntered(e -> {
            Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(btn.scaleXProperty(), 1)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(btn.scaleXProperty(), 1.05))
            );
            hoverIn.play();
        });
        
        btn.setOnMouseExited(e -> {
            Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(btn.scaleXProperty(), 1.05)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(btn.scaleXProperty(), 1))
            );
            hoverOut.play();
        });
        
        return btn;
    }

    // IMPROVED: Enhanced black theme for quick transaction dialog
    private void showQuickTransactionDialog(String transactionType) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Quick " + transactionType);
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.CENTER);
        
        // IMPROVED: Much darker black background with better contrast
        content.setStyle("-fx-background-color: linear-gradient(135deg, rgba(8, 8, 8, 0.98), rgba(18, 18, 18, 0.98)); " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.15); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 25; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 40, 0, 0, 25);");
        
        Text title = new Text("Quick " + transactionType);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.WHITE); // White text
        
        // Amount field
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountField.setPrefWidth(300);
        amountField.setPrefHeight(45);
        styleImprovedBlackTextField(amountField);
        setNumericInput(amountField);
        
        // Category field (only for expenses)
        ComboBox<String> categoryBox = null;
        if ("Expense".equals(transactionType)) {
            categoryBox = new ComboBox<>(categories);
            categoryBox.setValue(categories.get(0));
            categoryBox.setPrefWidth(300);
            categoryBox.setPrefHeight(45);
            styleImprovedBlackComboBox(categoryBox);
        }
        
        // Note field
        TextField noteField = new TextField();
        noteField.setPromptText("Note (optional)");
        noteField.setPrefWidth(300);
        noteField.setPrefHeight(45);
        styleImprovedBlackTextField(noteField);
        
        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button addBtn = createImprovedBlackButton("Add " + transactionType, "#10b981", true);
        addBtn.setPrefWidth(150);
        
        Button cancelBtn = createImprovedBlackButton("Cancel", "#333333", false);
        cancelBtn.setPrefWidth(100);
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(addBtn, cancelBtn);
        
        // Add all fields to content
        content.getChildren().addAll(title, amountField);
        if (categoryBox != null) {
            content.getChildren().add(categoryBox);
        }
        content.getChildren().addAll(noteField, buttons);
        
        // FIXED: Proper event handler for add button
        final ComboBox<String> finalCategoryBox = categoryBox;
        addBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String category = "Expense".equals(transactionType) && finalCategoryBox != null ? 
                                finalCategoryBox.getValue() : "Income";
                String note = noteField.getText();
                
                Transaction transaction = new Transaction(LocalDate.now(), transactionType, category, Math.abs(amount), note);
                transactions.add(0, transaction);
                saveUserTransactions();
                updateAllStats();
                
                showNotification(transactionType + " added successfully!", "#10b981");
                dialog.close();
            } catch (NumberFormatException ex) {
                showNotification("Please enter a valid amount", "#ef4444");
            }
        });
        
        Scene scene = new Scene(content, 400, 500);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(createMainAppCSS());
        
        dialog.setScene(scene);
        dialog.show();
        
        // Focus on amount field
        Platform.runLater(() -> amountField.requestFocus());
        
        // Dialog entrance animation
        content.setScaleX(0.8);
        content.setScaleY(0.8);
        content.setOpacity(0);
        
        Timeline entrance = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(content.scaleXProperty(), 0.8),
                new KeyValue(content.scaleYProperty(), 0.8),
                new KeyValue(content.opacityProperty(), 0)),
            new KeyFrame(Duration.seconds(0.3), 
                new KeyValue(content.scaleXProperty(), 1, Interpolator.EASE_OUT),
                new KeyValue(content.scaleYProperty(), 1, Interpolator.EASE_OUT),
                new KeyValue(content.opacityProperty(), 1))
        );
        entrance.play();
    }

    // NEW: Improved black dialog styling methods
    private void styleImprovedBlackTextField(TextField textField) {
        textField.setStyle("-fx-background-color: rgba(25, 25, 25, 0.95); " +
                          "-fx-text-fill: white; " +
                          "-fx-background-radius: 15; " +
                          "-fx-border-color: rgba(255, 255, 255, 0.25); " +
                          "-fx-border-radius: 15; " +
                          "-fx-border-width: 1; " +
                          "-fx-padding: 12; " +
                          "-fx-font-size: 14px; " +
                          "-fx-font-weight: 500; " +
                          "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);");
    }

    private void styleImprovedBlackComboBox(ComboBox<?> comboBox) {
        comboBox.setStyle("-fx-background-color: rgba(25, 25, 25, 0.95); " +
                         "-fx-text-fill: white; " +
                         "-fx-background-radius: 15; " +
                         "-fx-border-color: rgba(255, 255, 255, 0.25); " +
                         "-fx-border-radius: 15; " +
                         "-fx-border-width: 1; " +
                         "-fx-font-size: 14px; " +
                         "-fx-font-weight: 500;");
    }

    private Button createImprovedBlackButton(String text, String baseColor, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        
        button.setStyle("-fx-background-color: " + baseColor + "; " +
                       "-fx-text-fill: white; " +
                       "-fx-background-radius: 25; " +
                       "-fx-cursor: hand; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 4);");
        
        // Enhanced hover animations
        button.setOnMouseEntered(e -> {
            Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(button.scaleXProperty(), 1),
                    new KeyValue(button.scaleYProperty(), 1)),
                new KeyFrame(Duration.seconds(0.1), 
                    new KeyValue(button.scaleXProperty(), 1.05),
                    new KeyValue(button.scaleYProperty(), 1.05))
            );
            hoverIn.play();
            
            // Brighten color on hover
            if (isPrimary) {
                button.setStyle(button.getStyle() + "; -fx-background-color: derive(" + baseColor + ", 20%);");
            }
        });
        
        button.setOnMouseExited(e -> {
            Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(button.scaleXProperty(), 1.05),
                    new KeyValue(button.scaleYProperty(), 1.05)),
                new KeyFrame(Duration.seconds(0.1), 
                    new KeyValue(button.scaleXProperty(), 1),
                    new KeyValue(button.scaleYProperty(), 1))
            );
            hoverOut.play();
            
            // Reset color
            button.setStyle("-fx-background-color: " + baseColor + "; " +
                           "-fx-text-fill: white; " +
                           "-fx-background-radius: 25; " +
                           "-fx-cursor: hand; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 4);");
        });
        
        return button;
    }

    private HBox createChartsSection() {
        HBox section = new HBox(30);
        section.setPrefHeight(400);
        
        // Expense pie chart
        VBox pieChartCard = createChartCard("Expense Breakdown");
        expenseChart = new PieChart();
        expenseChart.setPrefSize(350, 300);
        expenseChart.setStyle("-fx-background-color: transparent;");
        updateExpenseChart();
        pieChartCard.getChildren().add(expenseChart);
        
        // Trend area chart
        VBox trendChartCard = createChartCard("Income vs Expenses Trend");
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        trendChart = new AreaChart<>(xAxis, yAxis);
        trendChart.setPrefSize(500, 300);
        trendChart.setStyle("-fx-background-color: transparent;");
        trendChart.setCreateSymbols(false);
        updateTrendChart();
        trendChartCard.getChildren().add(trendChart);
        
        section.getChildren().addAll(pieChartCard, trendChartCard);
        
        return section;
    }

    private VBox createChartCard(String title) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                     "-fx-background-radius: 20; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        Text cardTitle = new Text(title);
        cardTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        cardTitle.setFill(Color.WHITE);
        
        card.getChildren().add(cardTitle);
        
        return card;
    }

    private VBox createRecentTransactionsSection() {
        VBox section = new VBox(20);
        
        Text sectionTitle = new Text("Recent Transactions");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        sectionTitle.setFill(Color.WHITE);
        
        VBox transactionsCard = new VBox(10);
        transactionsCard.setPadding(new Insets(25));
        transactionsCard.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                 "-fx-background-radius: 20; " +
                                 "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        // Add recent transactions
        transactionsList = new VBox(8);
        updateRecentTransactions();
        
        transactionsCard.getChildren().add(transactionsList);
        section.getChildren().addAll(sectionTitle, transactionsCard);
        
        return section;
    }

    private void showTransactions() {
        VBox transactionsView = new VBox(25);
        transactionsView.setPadding(new Insets(30));
        
        Text title = new Text("All Transactions");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);
        
        // Filter controls
        HBox filterBox = createFilterControls();
        
        // Transactions list
        VBox transactionsList = new VBox(10);
        transactionsList.setPadding(new Insets(25));
        transactionsList.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                 "-fx-background-radius: 20; " +
                                 "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        updateAllTransactionsList(transactionsList);
        
        transactionsView.getChildren().addAll(title, filterBox, transactionsList);
        
        ScrollPane scroll = new ScrollPane(transactionsView);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        updateContentWithAnimation(scroll);
    }

    private HBox createFilterControls() {
        HBox filterBox = new HBox(20);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        
        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All", "Income", "Expense");
        typeFilter.setValue("All");
        typeFilter.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                           "-fx-text-fill: white; " +
                           "-fx-background-radius: 15;");
        
        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("All Categories");
        categoryFilter.getItems().addAll(categories);
        categoryFilter.setValue("All Categories");
        categoryFilter.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                               "-fx-text-fill: white; " +
                               "-fx-background-radius: 15;");
        
        DatePicker dateFrom = new DatePicker();
        DatePicker dateTo = new DatePicker();
        
        filterBox.getChildren().addAll(
            new Label("Type:"), typeFilter,
            new Label("Category:"), categoryFilter,
            new Label("From:"), dateFrom,
            new Label("To:"), dateTo
        );
        
        // Style labels
        filterBox.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setTextFill(Color.WHITE);
                ((Label) node).setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 14));
            }
        });
        
        return filterBox;
    }

    private void showAnalytics() {
        VBox analyticsView = new VBox(30);
        analyticsView.setPadding(new Insets(30));
        
        Text title = new Text("Financial Analytics");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);
        
        // Analytics cards
        HBox analyticsCards = createAnalyticsCards();
        
        // Detailed charts
        VBox detailedCharts = createDetailedCharts();
        
        analyticsView.getChildren().addAll(title, analyticsCards, detailedCharts);
        
        ScrollPane scroll = new ScrollPane(analyticsView);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        updateContentWithAnimation(scroll);
    }

    private HBox createAnalyticsCards() {
        HBox cards = new HBox(25);
        cards.setAlignment(Pos.CENTER);
        
        // Monthly summary card
        VBox monthlySummary = createAnalyticsCard("This Month", 
            calculateMonthlyIncome(), calculateMonthlyExpenses(), "#6366f1");
        
        // Weekly summary card
        VBox weeklySummary = createAnalyticsCard("This Week", 
            calculateWeeklyIncome(), calculateWeeklyExpenses(), "#10b981");
        
        // Average spending card
        VBox avgSpending = createAnalyticsCard("Daily Average", 
            0, calculateDailyAverage(), "#f59e0b");
        
        cards.getChildren().addAll(monthlySummary, weeklySummary, avgSpending);
        
        return cards;
    }

    private VBox createAnalyticsCard(String period, double income, double expense, String color) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: " + color + "; " +
                     "-fx-background-radius: 20; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 8);");
        
        Text periodText = new Text(period);
        periodText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        periodText.setFill(Color.WHITE);
        
        if (income > 0) {
            Text incomeText = new Text("Income: ₹" + String.format("%.0f", income));
            incomeText.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 16));
            incomeText.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.9));
            card.getChildren().add(incomeText);
        }
        
        Text expenseText = new Text("Expenses: ₹" + String.format("%.0f", expense));
        expenseText.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 16));
        expenseText.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.9));
        
        Text netText = new Text("Net: ₹" + String.format("%.0f", income - expense));
        netText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        netText.setFill(Color.WHITE);
        
        card.getChildren().addAll(periodText, expenseText, netText);
        
        return card;
    }

    private VBox createDetailedCharts() {
        VBox charts = new VBox(30);
        
        // Monthly trend chart
        VBox monthlyTrendCard = createChartCard("6-Month Trend");
        LineChart<String, Number> monthlyChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        monthlyChart.setPrefSize(800, 400);
        monthlyChart.setStyle("-fx-background-color: transparent;");
        updateMonthlyTrendChart(monthlyChart);
        monthlyTrendCard.getChildren().add(monthlyChart);
        
        charts.getChildren().add(monthlyTrendCard);
        
        return charts;
    }

    private void showBudget() {
        VBox budgetView = new VBox(30);
        budgetView.setPadding(new Insets(30));
        
        Text title = new Text("Budget Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);
        
        // Budget overview card
        VBox budgetCard = createBudgetOverviewCard();
        
        // Budget categories
        VBox categoriesCard = createBudgetCategoriesCard();
        
        budgetView.getChildren().addAll(title, budgetCard, categoriesCard);
        
        ScrollPane scroll = new ScrollPane(budgetView);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        updateContentWithAnimation(scroll);
    }

    private VBox createBudgetOverviewCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                     "-fx-background-radius: 20; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        Text cardTitle = new Text("Monthly Budget Overview");
        cardTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        cardTitle.setFill(Color.WHITE);
        
        // Budget progress
        double monthlyExpenses = calculateMonthlyExpenses();
        double budgetUsed = (monthlyExpenses / monthlyBudget) * 100;
        
        HBox budgetInfo = new HBox(40);
        budgetInfo.setAlignment(Pos.CENTER_LEFT);
        
        VBox budgetStats = new VBox(10);
        Text budgetAmount = new Text("Budget: ₹" + String.format("%.0f", monthlyBudget));
        budgetAmount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        budgetAmount.setFill(Color.WHITE);
        
        Text spentAmount = new Text("Spent: ₹" + String.format("%.0f", monthlyExpenses));
        spentAmount.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 18));
        spentAmount.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.8));
        
        Text remainingAmount = new Text("Remaining: ₹" + String.format("%.0f", monthlyBudget - monthlyExpenses));
        remainingAmount.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 18));
        remainingAmount.setFill(budgetUsed > 100 ? Color.RED : Color.LIGHTGREEN);
        
        budgetStats.getChildren().addAll(budgetAmount, spentAmount, remainingAmount);
        
        // Progress bar
        ProgressBar progressBar = new ProgressBar(Math.min(budgetUsed / 100, 1.0));
        progressBar.setPrefWidth(300);
        progressBar.setPrefHeight(20);
        progressBar.setStyle("-fx-accent: " + (budgetUsed > 100 ? "#ef4444" : "#10b981") + ";");
        
        Text progressText = new Text(String.format("%.1f%% used", budgetUsed));
        progressText.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 16));
        progressText.setFill(Color.WHITE);
        
        VBox progressSection = new VBox(10);
        progressSection.getChildren().addAll(progressBar, progressText);
        
        budgetInfo.getChildren().addAll(budgetStats, progressSection);
        
        Button setBudgetBtn = createModernButton("Update Budget", "#6366f1", true);
        setBudgetBtn.setPrefWidth(200);
        setBudgetBtn.setOnAction(e -> showBudgetDialog());
        
        card.getChildren().addAll(cardTitle, budgetInfo, setBudgetBtn);
        
        return card;
    }

    private VBox createBudgetCategoriesCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                     "-fx-background-radius: 20; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        Text cardTitle = new Text("Spending by Category");
        cardTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        cardTitle.setFill(Color.WHITE);
        
        VBox categoriesList = new VBox(15);
        
        Map<String, Double> categoryTotals = calculateCategoryTotals();
        categoryTotals.forEach((category, amount) -> {
            HBox categoryRow = createCategoryBudgetRow(category, amount);
            categoriesList.getChildren().add(categoryRow);
        });
        
        card.getChildren().addAll(cardTitle, categoriesList);
        
        return card;
    }

    private HBox createCategoryBudgetRow(String category, double amount) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15));
        row.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); " +
                     "-fx-background-radius: 15;");
        
        Text categoryIcon = new Text(getCategoryIcon(category));
        categoryIcon.setFont(Font.font(20));
        
        VBox categoryInfo = new VBox(5);
        Text categoryName = new Text(category);
        categoryName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        categoryName.setFill(Color.WHITE);
        
        double totalExpenses = calculateMonthlyExpenses();
        double percentage = totalExpenses > 0 ? (amount / totalExpenses) * 100 : 0;
        Text categoryPercentage = new Text(String.format("%.1f%% of expenses", percentage));
        categoryPercentage.setFont(Font.font("Segoe UI", 14));
        categoryPercentage.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
        
        categoryInfo.getChildren().addAll(categoryName, categoryPercentage);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Text amountText = new Text("₹" + String.format("%.0f", amount));
        amountText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        amountText.setFill(Color.WHITE);
        
        row.getChildren().addAll(categoryIcon, categoryInfo, spacer, amountText);
        
        return row;
    }

    private void showSettings() {
        VBox settingsView = new VBox(30);
        settingsView.setPadding(new Insets(30));
        
        Text title = new Text("Settings");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);
        
        // Settings cards
        VBox settingsCards = createSettingsCards();
        
        settingsView.getChildren().addAll(title, settingsCards);
        
        ScrollPane scroll = new ScrollPane(settingsView);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        updateContentWithAnimation(scroll);
    }

    private VBox createSettingsCards() {
        VBox cards = new VBox(20);
        
        // Profile settings
        VBox profileCard = createSettingsCard("Profile Settings", 
            "Manage your account information and preferences");
        
        // Data management
        VBox dataCard = createSettingsCard("Data Management", 
            "Export, backup, or clear your financial data");
        
        Button exportBtn = createModernButton("Export Data to CSV", "#6366f1", true);
        exportBtn.setPrefWidth(200);
        exportBtn.setOnAction(e -> exportData());
        dataCard.getChildren().add(exportBtn);
        
        // Appearance settings
        VBox appearanceCard = createSettingsCard("Appearance", 
            "Customize the look and feel of the application");
        
        cards.getChildren().addAll(profileCard, dataCard, appearanceCard);
        
        return cards;
    }

    private VBox createSettingsCard(String title, String description) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                     "-fx-background-radius: 20; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        Text cardTitle = new Text(title);
        cardTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        cardTitle.setFill(Color.WHITE);
        
        Text cardDescription = new Text(description);
        cardDescription.setFont(Font.font("Segoe UI", 14));
        cardDescription.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.8));
        
        card.getChildren().addAll(cardTitle, cardDescription);
        
        return card;
    }

    // ADD TRANSACTION DIALOG WITH MODERN DESIGN
    private void showAddTransactionDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Add New Transaction");
        
        VBox content = createTransactionDialogContent(dialog);
        
        Scene scene = new Scene(content, 500, 650);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(createMainAppCSS());
        
        dialog.setScene(scene);
        dialog.show();
        
        // Dialog entrance animation
        content.setScaleX(0.8);
        content.setScaleY(0.8);
        content.setOpacity(0);
        
        Timeline entrance = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(content.scaleXProperty(), 0.8),
                new KeyValue(content.scaleYProperty(), 0.8),
                new KeyValue(content.opacityProperty(), 0)),
            new KeyFrame(Duration.seconds(0.3), 
                new KeyValue(content.scaleXProperty(), 1, Interpolator.EASE_OUT),
                new KeyValue(content.scaleYProperty(), 1, Interpolator.EASE_OUT),
                new KeyValue(content.opacityProperty(), 1))
        );
        entrance.play();
    }

    private VBox createTransactionDialogContent(Stage dialog) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: linear-gradient(135deg, rgba(102, 126, 234, 0.9), rgba(118, 75, 162, 0.9)); " +
                        "-fx-background-radius: 25; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 25, 0, 0, 15);");
        
        Text title = new Text("Add Transaction");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.WHITE);
        
        // Form fields
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);
        
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Income", "Expense");
        typeBox.setValue("Expense");
        styleComboBox(typeBox);
        
        ComboBox<String> categoryBox = new ComboBox<>(categories);
        categoryBox.setValue(categories.get(0));
        styleComboBox(categoryBox);
        
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        styleTextField(amountField);
        setNumericInput(amountField);
        
        TextField noteField = new TextField();
        noteField.setPromptText("Note (optional)");
        styleTextField(noteField);
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        styleDatePicker(datePicker);
        
        form.getChildren().addAll(
            createFieldWithLabel("Type", typeBox),
            createFieldWithLabel("Category", categoryBox),
            createFieldWithLabel("Amount (₹)", amountField),
            createFieldWithLabel("Note", noteField),
            createFieldWithLabel("Date", datePicker)
        );
        
        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button addBtn = createModernButton("Add Transaction", "#10b981", true);
        addBtn.setPrefWidth(180);
        addBtn.setOnAction(e -> {
            if (addTransactionFromDialog(typeBox, categoryBox, amountField, noteField, datePicker)) {
                dialog.close();
            }
        });
        
        Button cancelBtn = createModernButton("Cancel", "transparent", false);
        cancelBtn.setPrefWidth(120);
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(addBtn, cancelBtn);
        
        content.getChildren().addAll(title, form, buttons);
        
        return content;
    }

    private VBox createFieldWithLabel(String labelText, Node field) {
        VBox container = new VBox(8);
        container.setAlignment(Pos.CENTER_LEFT);
        
        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 14));
        
        container.getChildren().addAll(label, field);
        return container;
    }

    private void styleComboBox(ComboBox<?> comboBox) {
        comboBox.setPrefWidth(400);
        comboBox.setPrefHeight(45);
        comboBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                         "-fx-text-fill: white; " +
                         "-fx-background-radius: 15; " +
                         "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                         "-fx-border-radius: 15; " +
                         "-fx-font-size: 14px;");
    }

    private void styleTextField(TextField textField) {
        textField.setPrefWidth(400);
        textField.setPrefHeight(45);
        textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                          "-fx-text-fill: white; " +
                          "-fx-background-radius: 15; " +
                          "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                          "-fx-border-radius: 15; " +
                          "-fx-padding: 12; " +
                          "-fx-font-size: 14px; " +
                          "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);");
    }

    private void styleDatePicker(DatePicker datePicker) {
        datePicker.setPrefWidth(400);
        datePicker.setPrefHeight(45);
        datePicker.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                           "-fx-background-radius: 15; " +
                           "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                           "-fx-border-radius: 15;");
    }

    // UTILITY METHODS
    private void updateContentWithAnimation(Node content) {
        mainContentArea.getChildren().clear();
        mainContentArea.getChildren().add(content);
        
        // Slide in animation
        content.setTranslateX(50);
        content.setOpacity(0);
        
        Timeline slideIn = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(content.translateXProperty(), 50),
                new KeyValue(content.opacityProperty(), 0)),
            new KeyFrame(Duration.seconds(0.4), 
                new KeyValue(content.translateXProperty(), 0, Interpolator.EASE_OUT),
                new KeyValue(content.opacityProperty(), 1))
        );
        slideIn.play();
    }

    private boolean addTransactionFromDialog(ComboBox<String> typeBox, ComboBox<String> categoryBox,
                                           TextField amountField, TextField noteField, DatePicker datePicker) {
        try {
            String type = typeBox.getValue();
            String category = categoryBox.getValue();
            double amount = Double.parseDouble(amountField.getText());
            String note = noteField.getText();
            LocalDate date = datePicker.getValue();
            
            Transaction transaction = new Transaction(date, type, category, Math.abs(amount), note);
            transactions.add(0, transaction);
            saveUserTransactions();
            updateAllStats();
            
            showNotification("Transaction added successfully!", "#10b981");
            return true;
        } catch (NumberFormatException e) {
            showNotification("Please enter a valid amount", "#ef4444");
            return false;
        }
    }

    private void showNotification(String message, String color) {
        // Create notification popup
        Label notification = new Label(message);
        notification.setTextFill(Color.WHITE);
        notification.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        notification.setPadding(new Insets(15, 25, 15, 25));
        notification.setStyle("-fx-background-color: " + color + "; " +
                             "-fx-background-radius: 25; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        
        StackPane notificationContainer = new StackPane(notification);
        notificationContainer.setAlignment(Pos.TOP_CENTER);
        notificationContainer.setPadding(new Insets(30));
        notificationContainer.setMouseTransparent(true);
        
        // Add to root pane temporarily
        StackPane rootStack = new StackPane();
        rootStack.getChildren().addAll(rootPane, notificationContainer);
        
        Scene currentScene = primaryStage.getScene();
        currentScene.setRoot(rootStack);
        
        // Animation
        notification.setTranslateY(-100);
        notification.setOpacity(0);
        
        Timeline showNotification = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(notification.translateYProperty(), -100),
                new KeyValue(notification.opacityProperty(), 0)),
            new KeyFrame(Duration.seconds(0.5), 
                new KeyValue(notification.translateYProperty(), 0),
                new KeyValue(notification.opacityProperty(), 1)),
            new KeyFrame(Duration.seconds(2.5), 
                new KeyValue(notification.translateYProperty(), 0),
                new KeyValue(notification.opacityProperty(), 1)),
            new KeyFrame(Duration.seconds(3), 
                new KeyValue(notification.translateYProperty(), -100),
                new KeyValue(notification.opacityProperty(), 0))
        );
        
        showNotification.setOnFinished(e -> currentScene.setRoot(rootPane));
        showNotification.play();
    }

    private void updateAllStats() {
        double totalIncome = transactions.stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
        
        double totalExpense = transactions.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
        
        incomeLabel.setText("Income\n₹" + String.format("%,.0f", totalIncome));
        expenseLabel.setText("Expenses\n₹" + String.format("%,.0f", totalExpense));
        balanceLabel.setText("Balance\n₹" + String.format("%+,.0f", totalIncome - totalExpense));
        
        updateExpenseChart();
        updateTrendChart();
        updateRecentTransactions();
        
        // Refresh current view if it's dashboard
        if ("Dashboard".equals(currentTab)) {
            showDashboard();
        }
    }

    private void updateExpenseChart() {
        if (expenseChart == null) return;
        
        Map<String, Double> categoryTotals = calculateCategoryTotals();
        
        expenseChart.getData().clear();
        if (categoryTotals.isEmpty()) {
            expenseChart.getData().add(new PieChart.Data("No expenses yet", 1));
        } else {
            categoryTotals.forEach((category, amount) -> {
                PieChart.Data slice = new PieChart.Data(category + " (₹" + String.format("%.0f", amount) + ")", amount);
                expenseChart.getData().add(slice);
            });
        }
    }

    private void updateTrendChart() {
        if (trendChart == null) return;
        
        trendChart.getData().clear();
        
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");
        
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        
        for (int i = 6; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            String dateStr = date.format(formatter);
            
            double dailyIncome = transactions.stream()
                    .filter(t -> "Income".equalsIgnoreCase(t.getType()) && t.getDate().equals(date))
                    .mapToDouble(Transaction::getAmount).sum();
            
            double dailyExpense = transactions.stream()
                    .filter(t -> "Expense".equalsIgnoreCase(t.getType()) && t.getDate().equals(date))
                    .mapToDouble(Transaction::getAmount).sum();
            
            incomeSeries.getData().add(new XYChart.Data<>(dateStr, dailyIncome));
            expenseSeries.getData().add(new XYChart.Data<>(dateStr, dailyExpense));
        }
        
        trendChart.getData().addAll(incomeSeries, expenseSeries);
    }

    private void updateRecentTransactions() {
        if (transactionsList == null) return;
        
        transactionsList.getChildren().clear();
        
        if (transactions.isEmpty()) {
            Label emptyLabel = new Label("No transactions yet");
            emptyLabel.setTextFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
            emptyLabel.setFont(Font.font("Segoe UI", 16));
            transactionsList.getChildren().add(emptyLabel);
            return;
        }
        
        List<Transaction> recentTransactions = transactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .limit(5)
                .collect(Collectors.toList());
        
        for (Transaction transaction : recentTransactions) {
            HBox transactionRow = createTransactionRow(transaction);
            transactionsList.getChildren().add(transactionRow);
        }
    }

    private void updateAllTransactionsList(VBox container) {
        container.getChildren().clear();
        
        if (transactions.isEmpty()) {
            Label emptyLabel = new Label("No transactions yet");
            emptyLabel.setTextFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
            emptyLabel.setFont(Font.font("Segoe UI", 16));
            container.getChildren().add(emptyLabel);
            return;
        }
        
        for (Transaction transaction : transactions) {
            HBox transactionRow = createTransactionRow(transaction);
            container.getChildren().add(transactionRow);
        }
    }

    private void updateMonthlyTrendChart(LineChart<String, Number> chart) {
        chart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Expenses");
        
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        
        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            String monthStr = monthDate.format(formatter);
            
            LocalDate monthStart = monthDate.withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            
            double monthlyExpense = transactions.stream()
                    .filter(t -> "Expense".equalsIgnoreCase(t.getType()) && 
                               !t.getDate().isBefore(monthStart) && 
                               !t.getDate().isAfter(monthEnd))
                    .mapToDouble(Transaction::getAmount).sum();
            
            series.getData().add(new XYChart.Data<>(monthStr, monthlyExpense));
        }
        
        chart.getData().add(series);
    }

    private HBox createTransactionRow(Transaction transaction) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15));
        row.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); " +
                     "-fx-background-radius: 12;");
        
        // Category icon
        Text icon = new Text(getCategoryIcon(transaction.getCategory()));
        icon.setFont(Font.font(24));
        
        // Transaction details
        VBox details = new VBox(3);
        Text category = new Text(transaction.getCategory());
        category.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        category.setFill(Color.WHITE);
        
        Text date = new Text(transaction.getDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        date.setFont(Font.font("Segoe UI", 12));
        date.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
        
        if (transaction.getNote() != null && !transaction.getNote().trim().isEmpty()) {
            Text note = new Text(transaction.getNote());
            note.setFont(Font.font("Segoe UI", 11));
            note.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.6));
            details.getChildren().addAll(category, note, date);
        } else {
            details.getChildren().addAll(category, date);
        }
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Amount
        Text amount = new Text();
        if ("Income".equalsIgnoreCase(transaction.getType())) {
            amount.setText("+₹" + String.format("%.2f", transaction.getAmount()));
            amount.setFill(Color.LIGHTGREEN);
        } else {
            amount.setText("-₹" + String.format("%.2f", transaction.getAmount()));
            amount.setFill(Color.SALMON);
        }
        amount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        
        // Delete button
        Button deleteBtn = new Button("×");
        deleteBtn.setPrefSize(25, 25);
        deleteBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        deleteBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.3); " +
                          "-fx-text-fill: #ef4444; " +
                          "-fx-background-radius: 15; " +
                          "-fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            transactions.remove(transaction);
            saveUserTransactions();
            updateAllStats();
            showNotification("Transaction deleted", "#ef4444");
        });
        
        row.getChildren().addAll(icon, details, spacer, amount, deleteBtn);
        
        // Hover effect
        row.setOnMouseEntered(e -> 
            row.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 12;"));
        row.setOnMouseExited(e -> 
            row.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 12;"));
        
        return row;
    }

    private String getCategoryIcon(String category) {
        switch (category.toLowerCase()) {
            case "food & dining": case "food": return "🍽️";
            case "transportation": return "🚗";
            case "shopping": return "🛍️";
            case "entertainment": return "🎬";
            case "bills & utilities": case "bills": return "📄";
            case "healthcare": return "🏥";
            case "education": return "📚";
            case "investment": return "📈";
            case "travel": return "✈️";
            case "income": return "💰";
            default: return "📋";
        }
    }

    // CALCULATION METHODS
    private double calculateMonthlyIncome() {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        
        return transactions.stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()) && 
                           !t.getDate().isBefore(monthStart) && 
                           !t.getDate().isAfter(now))
                .mapToDouble(Transaction::getAmount).sum();
    }

    private double calculateMonthlyExpenses() {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        
        return transactions.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()) && 
                           !t.getDate().isBefore(monthStart) && 
                           !t.getDate().isAfter(now))
                .mapToDouble(Transaction::getAmount).sum();
    }

    private double calculateWeeklyIncome() {
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.minusDays(7);
        
        return transactions.stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()) && 
                           t.getDate().isAfter(weekStart))
                .mapToDouble(Transaction::getAmount).sum();
    }

    private double calculateWeeklyExpenses() {
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.minusDays(7);
        
        return transactions.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()) && 
                           t.getDate().isAfter(weekStart))
                .mapToDouble(Transaction::getAmount).sum();
    }

    private double calculateDailyAverage() {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(monthStart, now) + 1;
        
        double monthlyExpenses = calculateMonthlyExpenses();
        return daysBetween > 0 ? monthlyExpenses / daysBetween : 0;
    }

    private Map<String, Double> calculateCategoryTotals() {
        return transactions.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    // DATA PERSISTENCE
    private void saveUserTransactions() {
        try {
            java.nio.file.Path userFolder = Paths.get(BASE_FOLDER, currentUserId);
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
            }
            
            java.nio.file.Path userDataFile = Paths.get(userFolder.toString(), currentUserId + ".csv");
            
            List<String> csvLines = new ArrayList<>();
            csvLines.add("date,type,category,amount,note");
            
            for (Transaction t : transactions) {
                String line = String.join(",",
                        t.getDate().toString(),
                        escapeCommas(t.getType()),
                        escapeCommas(t.getCategory()),
                        String.valueOf(t.getAmount()),
                        escapeCommas(t.getNote())
                );
                csvLines.add(line);
            }
            
            Files.write(userDataFile, csvLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserTransactions() {
        transactions.clear();
        try {
            java.nio.file.Path userFolder = Paths.get(BASE_FOLDER, currentUserId);
            java.nio.file.Path userDataFile = Paths.get(userFolder.toString(), currentUserId + ".csv");
            
            if (!Files.exists(userDataFile)) {
                addSampleData();
                return;
            }
            
            List<String> lines = Files.readAllLines(userDataFile);
            
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                
                try {
                    LocalDate date = LocalDate.parse(parts[0]);
                    String type = unescapeCommas(parts[1]);
                    String category = unescapeCommas(parts[2]);
                    double amount = Double.parseDouble(parts[3]);
                    String note = unescapeCommas(parts[4]);
                    
                    transactions.add(new Transaction(date, type, category, amount, note));
                } catch (Exception e) {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            addSampleData();
        }
    }

    private void addSampleData() {
        LocalDate today = LocalDate.now();
        transactions.addAll(Arrays.asList(
            new Transaction(today, "Income", "Income", 45000, "Monthly salary"),
            new Transaction(today.minusDays(1), "Expense", "Food & Dining", 350, "Restaurant dinner"),
            new Transaction(today.minusDays(2), "Expense", "Transportation", 120, "Uber ride"),
            new Transaction(today.minusDays(3), "Expense", "Shopping", 2500, "Grocery shopping"),
            new Transaction(today.minusDays(4), "Expense", "Entertainment", 800, "Movie night"),
            new Transaction(today.minusDays(5), "Expense", "Bills & Utilities", 1500, "Internet bill")
        ));
        saveUserTransactions();
    }

    // AUTHENTICATION
    private void handleLogin(String userId, String password, Button loginBtn, Label messageLabel) {
        if (userId.isEmpty() || password.isEmpty()) {
            showMessage(messageLabel, "Please fill all fields", true);
            return;
        }
        
        animateButton(loginBtn, "Logging in...");
        
        Thread loginThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                if (authenticateUser(userId, password)) {
                    Platform.runLater(() -> {
                        currentUserId = userId;
                        showMainApp();
                    });
                } else {
                    Platform.runLater(() -> {
                        showMessage(messageLabel, "Invalid credentials", true);
                        resetButton(loginBtn, "Login");
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showMessage(messageLabel, "Login failed: " + e.getMessage(), true);
                    resetButton(loginBtn, "Login");
                });
            }
        });
        loginThread.start();
    }

    private void handleRegister(String userId, String password, String confirmPassword, 
                               Button createBtn, Label messageLabel, VBox loginForm, VBox registerForm) {
        if (userId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage(messageLabel, "Please fill all fields", true);
            return;
        }
        
        if (userId.length() < 3) {
            showMessage(messageLabel, "Username must be at least 3 characters", true);
            return;
        }
        
        if (password.length() < 6) {
            showMessage(messageLabel, "Password must be at least 6 characters", true);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showMessage(messageLabel, "Passwords do not match", true);
            return;
        }
        
        animateButton(createBtn, "Creating...");
        
        Thread registerThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                String result = registerUser(userId, password);
                Platform.runLater(() -> {
                    if (result.equals("SUCCESS")) {
                        showMessage(messageLabel, "Account created successfully!", false);
                        
                        Timeline switchDelay = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> {
                            // Switch to login form
                            Timeline hideRegister = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(registerForm.opacityProperty(), 1)),
                                new KeyFrame(Duration.seconds(0.3), new KeyValue(registerForm.opacityProperty(), 0))
                            );
                            hideRegister.setOnFinished(event -> {
                                registerForm.setVisible(false);
                                loginForm.setVisible(true);
                                Timeline showLogin = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(loginForm.opacityProperty(), 0)),
                                    new KeyFrame(Duration.seconds(0.3), new KeyValue(loginForm.opacityProperty(), 1))
                                );
                                showLogin.play();
                                resetButton(createBtn, "Create Account");
                            });
                            hideRegister.play();
                        }));
                        switchDelay.play();
                    } else {
                        showMessage(messageLabel, result, true);
                        resetButton(createBtn, "Create Account");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showMessage(messageLabel, "Registration failed", true);
                    resetButton(createBtn, "Create Account");
                });
            }
        });
        registerThread.start();
    }

    private String registerUser(String userId, String password) throws Exception {
        try {
            if (userExists(userId)) {
                return "Username already exists";
            }
            
            String userRecord = String.join(",",
                userId, password, userId.toLowerCase() + "@financeflow.com",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            Files.write(Paths.get(ACCOUNTS_FILE), Arrays.asList(userRecord),
                       StandardOpenOption.APPEND);
            
            java.nio.file.Path userFolder = Paths.get(BASE_FOLDER, userId);
            Files.createDirectories(userFolder);
            
            java.nio.file.Path userTransactionsFile = Paths.get(userFolder.toString(), userId + ".csv");
            Files.write(userTransactionsFile, Arrays.asList("date,type,category,amount,note"));
            
            return "SUCCESS";
        } catch (Exception e) {
            throw new Exception("Registration failed");
        }
    }

    private boolean userExists(String userId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ACCOUNTS_FILE));
            return lines.stream().skip(1).anyMatch(line -> line.startsWith(userId + ","));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean authenticateUser(String userId, String password) throws Exception {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ACCOUNTS_FILE));
            return lines.stream().skip(1).anyMatch(line -> {
                String[] parts = line.split(",");
                return parts.length >= 2 && parts[0].equals(userId) && parts[1].equals(password);
            });
        } catch (Exception e) {
            throw new Exception("Authentication failed");
        }
    }

    // UTILITY METHODS
    private void animateButton(Button button, String newText) {
        button.setDisable(true);
        button.setText(newText);
        
        Timeline pulse = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 1)),
            new KeyFrame(Duration.seconds(0.5), new KeyValue(button.scaleXProperty(), 0.95)),
            new KeyFrame(Duration.seconds(1), new KeyValue(button.scaleXProperty(), 1))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();
        
        button.setUserData(pulse); // Store animation reference
    }

    private void resetButton(Button button, String originalText) {
        button.setDisable(false);
        button.setText(originalText);
        
        // Stop pulse animation if it exists
        Object pulseAnimation = button.getUserData();
        if (pulseAnimation instanceof Timeline) {
            ((Timeline) pulseAnimation).stop();
            button.setScaleX(1);
            button.setScaleY(1);
        }
    }

    private void showMessage(Label label, String message, boolean isError) {
        label.setText(message);
        label.setTextFill(isError ? Color.RED : Color.LIGHTGREEN);
        label.setVisible(true);
        
        // Fade in animation
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(label.opacityProperty(), 0)),
            new KeyFrame(Duration.seconds(0.3), new KeyValue(label.opacityProperty(), 1))
        );
        fadeIn.play();
        
        // Auto-hide after 4 seconds
        Timeline autoHide = new Timeline(new KeyFrame(Duration.seconds(4), evt -> {
            Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(label.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(label.opacityProperty(), 0))
            );
            fadeOut.setOnFinished(e -> label.setVisible(false));
            fadeOut.play();
        }));
        autoHide.play();
    }

    private void showBudgetDialog() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf((int)monthlyBudget));
        dialog.setTitle("Update Budget");
        dialog.setHeaderText("Set your monthly budget:");
        dialog.setContentText("Budget (₹):");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(budgetStr -> {
            try {
                double newBudget = Double.parseDouble(budgetStr);
                if (newBudget > 0) {
                    monthlyBudget = newBudget;
                    showNotification("Budget updated to ₹" + String.format("%.0f", monthlyBudget), "#10b981");
                    // Refresh budget view if currently showing
                    if ("Budget".equals(currentTab)) {
                        showBudget();
                    }
                }
            } catch (NumberFormatException e) {
                showNotification("Invalid budget amount", "#ef4444");
            }
        });
    }

    private void exportData() {
        try {
            String fileName = currentUserId + "_finance_export_" + 
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            java.nio.file.Path exportPath = Paths.get(BASE_FOLDER, fileName);
            
            List<String> csvLines = new ArrayList<>();
            csvLines.add("Date,Type,Category,Amount,Note");
            
            for (Transaction t : transactions) {
                String line = String.join(",",
                        t.getDate().toString(),
                        escapeCommas(t.getType()),
                        escapeCommas(t.getCategory()),
                        String.valueOf(t.getAmount()),
                        escapeCommas(t.getNote())
                );
                csvLines.add(line);
            }
            
            Files.write(exportPath, csvLines);
            showNotification("Data exported successfully to " + fileName, "#10b981");
            
        } catch (Exception e) {
            showNotification("Export failed: " + e.getMessage(), "#ef4444");
        }
    }

    private void setNumericInput(TextField tf) {
        tf.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*(\\.\\d*)?")) {
                tf.setText(oldV);
            }
        });
    }

    private String escapeCommas(String text) {
        if (text == null) return "";
        return text.replace(",", "&#44;").replace("\"", "&quot;");
    }

    private String unescapeCommas(String text) {
        if (text == null) return "";
        return text.replace("&#44;", ",").replace("&quot;", "\"");
    }

    // STYLING METHODS
    private Paint createTextGradient() {
        return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(1, Color.web("#764ba2")));
    }

    private Background createAnimatedBackground() {
        return new Background(new BackgroundFill(
            new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(0.5, Color.web("#764ba2")),
                new Stop(1, Color.web("#f093fb"))),
            CornerRadii.EMPTY, Insets.EMPTY));
    }

    private Background createMainAppBackground() {
        return new Background(new BackgroundFill(
            new LinearGradient(45, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1a1a2e")),
                new Stop(0.5, Color.web("#16213e")),
                new Stop(1, Color.web("#0f3460"))),
            CornerRadii.EMPTY, Insets.EMPTY));
    }

    private void startBackgroundAnimation() {
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
        }
        
        backgroundAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, e -> updateBackgroundGradient(0)),
            new KeyFrame(Duration.seconds(10), e -> updateBackgroundGradient(1)),
            new KeyFrame(Duration.seconds(20), e -> updateBackgroundGradient(2)),
            new KeyFrame(Duration.seconds(30), e -> updateBackgroundGradient(0))
        );
        backgroundAnimation.setCycleCount(Timeline.INDEFINITE);
        backgroundAnimation.play();
    }

    private void updateBackgroundGradient(int phase) {
        LinearGradient gradient;
        switch (phase) {
            case 1:
                gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#f093fb")),
                    new Stop(0.5, Color.web("#f5576c")),
                    new Stop(1, Color.web("#4facfe")));
                break;
            case 2:
                gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#4facfe")),
                    new Stop(0.5, Color.web("#00f2fe")),
                    new Stop(1, Color.web("#667eea")));
                break;
            default:
                gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#667eea")),
                    new Stop(0.5, Color.web("#764ba2")),
                    new Stop(1, Color.web("#f093fb")));
                break;
        }
        
        if (rootPane != null) {
            rootPane.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private String createModernCSS() {
        return """
            .root {
                -fx-font-family: 'Segoe UI', sans-serif;
            }
            
            .text-field, .password-field {
                -fx-text-fill: white;
                -fx-prompt-text-fill: rgba(255, 255, 255, 0.6);
                -fx-highlight-fill: rgba(255, 255, 255, 0.2);
                -fx-highlight-text-fill: white;
            }
            
            .combo-box {
                -fx-text-fill: white;
            }
            
            .combo-box .list-cell {
                -fx-text-fill: white;
                -fx-background-color: rgba(0, 0, 0, 0.8);
            }
            
            .combo-box .list-cell:hover {
                -fx-background-color: rgba(255, 255, 255, 0.1);
            }
            
            .date-picker .text-field {
                -fx-text-fill: white;
            }
            
            .date-picker .arrow-button {
                -fx-background-color: rgba(255, 255, 255, 0.2);
            }
            
            .chart {
                -fx-background-color: transparent;
            }
            
            .chart-plot-background {
                -fx-background-color: transparent;
            }
            
            .chart-legend {
                -fx-background-color: rgba(255, 255, 255, 0.1);
                -fx-text-fill: white;
            }
            
            .axis {
                -fx-tick-label-fill: white;
            }
            
            .axis-label {
                -fx-text-fill: white;
            }
            """;
    }

    private String createMainAppCSS() {
        return createModernCSS() + """
            .scroll-pane {
                -fx-background: transparent;
                -fx-background-color: transparent;
            }
            
            .scroll-pane .viewport {
                -fx-background-color: transparent;
            }
            
            .scroll-pane .content {
                -fx-background-color: transparent;
            }
            
            .scroll-bar {
                -fx-background-color: rgba(255, 255, 255, 0.1);
            }
            
            .scroll-bar .thumb {
                -fx-background-color: rgba(255, 255, 255, 0.3);
                -fx-background-radius: 10;
            }
            
            .progress-bar .bar {
                -fx-background-color: linear-gradient(to right, #10b981, #34d399);
                -fx-background-radius: 10;
            }
            
            .progress-bar .track {
                -fx-background-color: rgba(255, 255, 255, 0.2);
                -fx-background-radius: 10;
            }
            """;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
