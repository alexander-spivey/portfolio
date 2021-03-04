using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TutorialAssets : MonoBehaviour
{
    private bool finished = false;
    public GameObject healthBar;
    public GameObject skullBar;

    void Start()
    {
        MainMenu.level = 2;
        GameManager.IsInputEnabled = false;
        healthBar.SetActive(false);
        skullBar.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        if (finished == false)
        {
            GameManager.IsInputEnabled = false;
        }
    }
    void OnCollisionEnter2D(Collision2D collision)
    {
        if (collision.collider.tag == "Ground")
        {
            GameManager.IsInputEnabled = true;
            finished = true;
        }
    }
    private void OnTriggerEnter2D(Collider2D collision)
    {
        if(collision.gameObject.name == "HealthUi" || LevelTriggers.lvl2 == true)
        {
            healthBar.SetActive(true);
        }
        if (collision.gameObject.name == "SkullBarUi" || LevelTriggers.lvl2 == true)
        {
            skullBar.SetActive(true);
        }

    }
}
