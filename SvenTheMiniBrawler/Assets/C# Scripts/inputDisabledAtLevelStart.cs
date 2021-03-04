using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class inputDisabledAtLevelStart : MonoBehaviour
{
    private bool finished = false;
    void Start()
    {
        GameManager.IsInputEnabled = false;
    }

    // Update is called once per frame
    void Update()
    {
        if(finished == false)
        {
            GameManager.IsInputEnabled = false;
        } 
    }
    void OnCollisionEnter2D(Collision2D collision)
    {
        if(collision.collider.tag == "Ground")
        {
            GameManager.IsInputEnabled = true;
            finished = true;
        }
    }
}
