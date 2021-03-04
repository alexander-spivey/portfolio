using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class textAppear : MonoBehaviour
{
    // Start is called before the first frame update
    public GameObject text;

    void Start()
    {
        text.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    void OnTriggerEnter2D(Collider2D collision)
    {
       if(collision.gameObject.tag == "Player")
        {
            text.SetActive(true);
        }
    }
    private void OnTriggerExit2D(Collider2D collision)
    {
        if (collision.gameObject.tag == "Player")
        {
            text.SetActive(false);
        }
    }
}
