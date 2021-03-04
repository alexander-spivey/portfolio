using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class MainMenu : MonoBehaviour
{
    public static int level = 1;
    //insert tutorial button (lvl 2)
    //real game start at lvl 3
    public void playGame()
    {
        level = 3;
        SceneManager.LoadScene(level);
    }

    public void quitGame()
    {
        Application.Quit();
    }
    public void retryGame()
    {
        SceneManager.LoadScene(level);
    }
    public void tutorialGame()
    {
        level = 2;
        SceneManager.LoadScene(level);
    }
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
