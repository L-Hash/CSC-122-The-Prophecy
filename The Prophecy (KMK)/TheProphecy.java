import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Dimension.*;

/*
 * A simple sample game to show how to create GUIs and process text-based user input.
 * 
 * Last updated: 2015-04-21
 * Original author: Dr. Jean Gourd
 * Author: Lucas Hashimoto
 */
public class TheProphecy
{
    private static final String GAME_TITLE = "The Prophecy";

    // the GUI components
    private JFrame frame;
    private JPanel content;
    private JLabel image_pane;
    private BufferedImage image;
    private JTextArea text_pane;
    private JTextField input_pane;

    // state
    private Area current_area;
    private ArrayList<String> inventory;
    
    // constructor
    public TheProphecy()
    {
        // initialize an empty inventory, add the areas, make the GUI components, set the current area, and start the game
        inventory = new ArrayList<String>();
        addAreas();
        makeFrame();
        setArea();
        startGame();
    }

    // adds the areas
    private void addAreas()
    {
        Area house, courtyard, tent, crossroads, forest, quicksand, clearing;
        
        // set the names and images
        house = new Area("your house.", "images/house.jpg");
        courtyard = new Area("the courtyard", "images/courtyard.jpg");
        tent = new Area("the prophet's tent", "images/tent.jpg");
        crossroads = new Area("the crossroads", "images/crossroads.jpg");
        forest = new Area("the forest", "images/forest.jpg");
       
        // add the exits, items, and grabbables
        house.addExit("courtyard", courtyard);
        house.addDescription("around","");
        house.addItem("journal", "  Something about going to see the Prophet.");
        house.addItem("desk", "  It is made of a rich mahogony. Your personal journal rests upon it.");
        house.addItem("bed", "  It isn't the comfiest bed, but it's served you well so far");
        
        courtyard.addExit("house", house);
        courtyard.addExit("tent", tent);
        courtyard.addExit("crossroads", crossroads);
        courtyard.addItem("fountain", "  It is a nice fountain.");
        courtyard.addItem("villager", "  He looks like he has to tell you something.");
        courtyard.addTalkable("villager", "  Villager: The prophet wants to see you in his tent.");
        
        tent.addExit("courtyard", courtyard);
        tent.addItem("bookshelves", "  They are empty.  Go figure.");
        tent.addItem("potions", "  They look dangerous");
        tent.addItem("crystal ball", "  The ball contains a swirling misting cloud.");
        
        tent.addTalkable("prophet", "  Prophet: Prophecy..blah blah. Here. Use this saw to enter the forest.");
    
        
        crossroads.addExit("village", courtyard);
        crossroads.addExit("forest", null);
        crossroads.addExit("bay", null);
        crossroads.addExit("cave", null);
        crossroads.addItem("sign", "  The sign is pointing in three directions. The Forest, The Bay, The Cave");

        
       
        
        // set the current area (house initially)
        current_area = house;
    }
    
    // sets the current area (image and text)
    private void setArea()
    {
        // set the description
        setDescription("");
        
        // set the image
        try
        {
            // death means the skull
            if (current_area == null)
            {
                image = ImageIO.read(new File("images/skull.jpg"));
            }
            // otherwise, load the appropriate area image
            else
            {
                image = ImageIO.read(new File(current_area.getImage()));
            }
            
            image_pane.setIcon(new ImageIcon(image));
        }
        catch (Exception e)
        {}        
        
        // pack the frame so the new image is rendered and the window is resized
        frame.pack();
    }
    
    // set the area description
    private void setDescription(String s)
    {
        // death means death
        if (current_area == null)
        {
            text_pane.setText("You are dead.");
        }
        else
        {
            text_pane.setText(current_area + "\n  You are carrying: " + inventory + "\n\n" + s);
        }
    }
    
    // starts the game
    private void startGame()
    {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        
        // position the window in the center of the screen, make it visible, and give the input_pane focus
        frame.setLocation(d.width / 2 - frame.getWidth() / 2, d.height / 2 - frame.getHeight() / 2);
        frame.setVisible(true);
        input_pane.requestFocus();
    }
    
    // makes the GUI components
    private void makeFrame()
    {
        // the frame and main content pane
        frame = new JFrame(GAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content = (JPanel)frame.getContentPane();
        
        // the image pane
        image_pane = new JLabel();
        image_pane.setPreferredSize(new Dimension(1000,500));
        image_pane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        content.add(image_pane, BorderLayout.LINE_START);
        
        // the text pane
        text_pane = new JTextArea(25, 30);
        text_pane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK));
        text_pane.setEditable(false);
        text_pane.setLineWrap(true);
        text_pane.setFont(new Font("TimesRoman", 1, 20));
        content.add(text_pane, BorderLayout.LINE_END);
        
        /// the input pane
        input_pane = new JTextField();
        input_pane.setFont(new Font("TimesRoman", 1, 20));
        input_pane.setColumns(35);
        input_pane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
        content.add(input_pane, BorderLayout.PAGE_END);
        
        // add a listener to the input pane so that actions can occur when the user presses enter
        input_pane.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // process the input and clear the input pane
                process(input_pane.getText());
                input_pane.setText("");
            }
        });
    }
    
    // process user input from the input pane
    private void process(String s)
    {
        String sl = s.toLowerCase().trim(); // the lowercase version of the user input
        String[] words;                     // user input split into words
        String verb;                        // the specified verb
        String noun;                        // the specified noun
        String response = "I don't understand.  Try:\n<verb> <noun>\nValid <verb>: go, look, take, talk, or open";
        
        // handle quitting
        if (sl.equals("quit") || sl.equals("exit") || sl.equals("bye"))
        {
            System.exit(0);
        }
 
        // if we've already died, simply get out of here
        // this allows the user to still quit, exit, or bye above
        if (current_area == null)
        {
            return;
        }
        
        // split the input into words
        words = sl.split(" ");
        
        // only accept two words (verb and noun)
        if (words.length == 2)
        {
            verb = words[0];
            noun = words[1];

            // act based on the verb
            if (verb.equals("go"))
            {
                // set a default response
                response = "Invalid exit.";
                
                // iterate through the keys and values of exits in the current area
                for (Map.Entry<String,Area> entry : current_area.getExits().entrySet())
                {
                    // get the exit
                    String exit = entry.getKey();
                    
                    // if it's the one chosen
                    if (noun.equals(exit))
                    {
                        // /change the current area and set it on the GUI
                        current_area = entry.getValue();
                        setArea();
                        response = "";
                        
                        break;
                    }
                }
            }
            else if (verb.equals("look"))
            {
                response = "I don't see that item.";
             
                // iterate through the keys and values of the items in the current area
                for (Map.Entry<String,String> entry : current_area.getItems().entrySet())
                {
                    // get the item
                    String item = entry.getKey();
      
                    // if it's the one chosen
                    if (noun.equals(item))
                    {
                        // set the response to the item's description
                        response = entry.getValue();
                        
                        break;
                    }
                }
           
            }
            else if (verb.equals("take"))
            {
                response = "You cannot take this item.";
                
                // iterate through the grabbables in the current area
                for (String grabbable : current_area.getGrabbables())
                {
                    // if it's the one chosen
                    if (noun.equals(grabbable))
                    {
                        // add it to the inventory
                        inventory.add(grabbable);
                        // remove it from the area's grabbables
                        current_area.removeGrabbable(grabbable);
                        response = "Item grabbed.";
                        
                        break;
                    }
                }
                for (Map.Entry<String,String> entry : current_area.getItems().entrySet())
                {
                    // get the item
                    String item = entry.getKey();
                    
                    // if it's the one chosen
                    if (noun.equals(item))
                    {
                        response = "I do not see that item.";
                        
                        break;
                    }
                }
                for (Map.Entry<String,String> entry : current_area.getTalkables().entrySet())
                {
                    // get the talkable
                    String talkable = entry.getKey();
                    
                    // if it's the one chosen
                    if (noun.equals(talkable))
                    {
                        // set the response to the talkable's dialouge
                        response = "You cannot take a person.";
                        
                        break;
                    }
                }
            }
            else if (verb.equals("talk"))
            {
                response = "Do you normally talk to inanimate objects?";
             
                // iterate through the keys and values of the items in the current area
                for (Map.Entry<String,String> entry : current_area.getTalkables().entrySet())
                {
                    // get the talkable
                    String talkable = entry.getKey();
                    String key_item = entry.getKey();
                    
                    // if it's the one chosen
                    if (noun.equals(talkable))
                    {
                        // set the response to the talkable's dialouge
                        response = entry.getValue();
                        
                        if (talkable == "prophet")
                        {
                            Area tent;
                            tent = new Area("the prophet's tent", "images/tent.jpg");
                            Area crossroads;
                            crossroads = new Area("the crossroads", "images/crossroads.jpg");
                            
                            crossroads.addKeyItem("saw");
                            key_item = "saw";
                            inventory.add(key_item);
                        }
                        
                        break;
                    }
                }
            }
            else if (verb.equals("use"))
            {
                response = "You can't use this.";
                
                for (String key_item : current_area.getKeyItems())
                {
                    
                    // if it's the one chosen
                    if (noun.equals(key_item))
                    {
                        // set the response to the talkable's dialouge
                         
                        if (key_item == "saw")
                        {
                            Area crossroads, forest;
                            crossroads = new Area("the crossroads", "images/crossroads.jpg");
                            forest = new Area("the forest", "images/forest.jpg");
                            
                            crossroads.addExit("forest", forest);
                            response = "The saw slices through the tree."; 
                        }
                        
                        
                        
                        break;
                    }
                }
            }
        }
        
        // update the description based on the response generated from the user's input
        setDescription(response);
    }
}