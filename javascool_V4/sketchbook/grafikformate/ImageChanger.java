import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.net.URL;

public class ImageChanger extends Applet implements ItemListener { // , ActionListener{//, MouseListener{
  private ImagePanel[] imagePanel = new ImagePanel[2];
  private ImageView imageView = new ImageView();
  private Image orgImage;
  private InfoView infoView = new InfoView();
  private Panel topPanel = new Panel();

  private Choice imageMenu = new Choice();
  private String[] bilder = new String[] { "bug.jpg", "text.jpg", "tasten.jpg", "bond.jpg" };

  private Button calcButton = new Button("Calculer la taille de l'image");
  private Calculator calculator;
  private Panel calcPanel = new Panel();

  public ImageChanger() {}
  public void init() {
    this.setName("Image Changer");
    this.setLayout(new GridBagLayout());
    this.setBackground(new Color(190, 190, 190));

    try {
      MediaTracker tracker = new MediaTracker(this);
      int index = (getDocumentBase().toString()).lastIndexOf("/");
      URL tmpURL = new URL((getDocumentBase().toString()).substring(0, index + 1) + "icons/");
      // System.out.println("url:"+tmpURL);
      orgImage = getImage(tmpURL, "bug.jpg");
      // vthierry patch: to access image in jar icons path
      orgImage = getImage(Thread.currentThread().getContextClassLoader().getResource("icons/bug.jpg"));
      tracker.addImage(orgImage, 0);
      try { tracker.waitForID(0);
      } catch(InterruptedException e) {}
    } catch(Exception e) {
      System.out.println("fuck");
    }
    topPanel.setLayout(new GridBagLayout());
    topPanel.setBackground(new Color(210, 210, 210));
    imageView.setImage(orgImage);
    imageView.setSize(orgImage.getWidth(this) + 20, orgImage.getHeight(this) + 20);
    infoView.setSize(200, orgImage.getHeight(this) - 20);
    for(int i = 0; i < 4; i++)
      imageMenu.add(bilder[i]);
    imageMenu.addItemListener(this);

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(10, 5, 2, 5);
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.CENTER;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    topPanel.add(imageMenu, c);
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.CENTER;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 2;
    c.gridheight = 5;
    c.weightx = c.weighty = 0;
    topPanel.add(imageView, c);
    c.gridx = 3;
    c.gridy = 0;
    c.gridwidth = 3;
    c.gridheight = 5;
    c.weightx = c.weighty = 1;
    topPanel.add(infoView, c);

    c.gridx = 0;
    c.gridy = 5;
    c.gridwidth = 3;
    c.gridheight = 1;
    c.weightx = c.weighty = 0;
    for(int i = 0; i < 2; i++) {
      imagePanel[i] = new ImagePanel(this, orgImage, i);
      imagePanel[i].setSize(600, orgImage.getHeight(this) + 20);
    }
    GridBagConstraints cMain = new GridBagConstraints();
    cMain.fill = GridBagConstraints.BOTH;
    cMain.insets = new Insets(1, 5, 1, 5);
    cMain.gridx = 0;
    cMain.gridy = 0;
    cMain.gridwidth = 1;
    cMain.gridheight = 1;
    cMain.weightx = cMain.weighty = 0;
    this.add(topPanel, cMain);
    cMain.gridy = 1;
    this.add(imagePanel[0], cMain);
    cMain.gridy = 2;
    this.add(imagePanel[1], cMain);
    cMain.gridy = 3;
    cMain.anchor = GridBagConstraints.CENTER;
    cMain.fill = GridBagConstraints.BOTH;
    calcPanel.setBackground(Color.red);
    calcPanel.add(calcButton);

    this.add(calcPanel, cMain);
  }
  public void setInfoText(String text) {
    infoView.setText(text);
    infoView.repaint();
  }
  public Image getOtherImage(int _number) {
    _number = 1 - _number;
    return imagePanel[_number].getCurrentImage();
  }
  public Image getOriginal() {
    return orgImage;
  }
  public void itemStateChanged(ItemEvent e) {
    String imageName = (String) e.getItem();
    try {
      MediaTracker tracker = new MediaTracker(this);
      int index = (getDocumentBase().toString()).lastIndexOf("/");
      URL tmpURL = new URL((getDocumentBase().toString()).substring(0, index + 1) + "icons/");
      orgImage = getImage(tmpURL, imageName);
      // vthierry patch: to access image in jar icons path
      orgImage = getImage(Thread.currentThread().getContextClassLoader().getResource("icons/" + imageName));
      tracker.addImage(orgImage, 0);
      try { tracker.waitForID(0);
      } catch(InterruptedException ex) {}
    } catch(Exception ex) {}
    imageView.setImage(orgImage);
    for(int i = 0; i < 2; i++)
      imagePanel[i].setImage(orgImage);
    this.repaint();
  }
  public boolean action(Event e, Object arg) {
    if(e.target == calcButton) {
      calculator = new Calculator();
    }
    return true;
  }
}
