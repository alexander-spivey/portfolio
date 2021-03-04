using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LevelTriggers : MonoBehaviour
{
    public SkullBar skullBar;
    public GameObject greenPortal1;
    public GameObject lvlIncomplete1;
    public GameObject camera1;

    public static bool lvl2 = false;

    // Start is called before the first frame update
    void Start()
    {
        if(MainMenu.level == 2 && lvl2 == false)
        {
            greenPortal1.SetActive(false);
            lvlIncomplete1.SetActive(true);
        }
        else if (MainMenu.level == 2 && lvl2 == true)
        {
            camera1.transform.position = new Vector3(32f, 12f, -10f);
            transform.position = new Vector3(32f, 12f, -39.78555f);
            greenPortal1.SetActive(true);
            lvlIncomplete1.SetActive(false);
        }
    }

    // Update is called once per frame
    void Update()
    {
        if (SkullBar.kills == skullBar.maxKills && MainMenu.level == 2)
        {
            greenPortal1.SetActive(true);
            lvlIncomplete1.SetActive(false);
        }
    }

    private void OnTriggerStay2D(Collider2D collision)
    {
        if (collision.gameObject.name == "GreenPortal1" && SkullBar.kills == skullBar.maxKills) 
        {
            if (Input.GetKey(KeyCode.Z))
            {
                MainMenu.level = 3;
                lvl2 = true;
                SceneManager.LoadScene(MainMenu.level);
            }
        }
        if (collision.gameObject.name == "GreenPortal1(BACK)")
        {
            if (Input.GetKey(KeyCode.Z))
            {   
                MainMenu.level = 2;
                SceneManager.LoadScene(MainMenu.level);
            }
        }
    }
}
