using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class PlayerHealth : MonoBehaviour
{
    public HealthBar healthBar;
    private Rigidbody2D rb;

    public float knockBack = 7;
    public float knockTime;
    public float startKnockTime;

    public int maxHealth = 10;
    public int currentHealth;
    private bool knockingBack = false;
    // Start is called before the first frame update
    void Start()
    {
        currentHealth = maxHealth;
        healthBar.SetMaxHealth(maxHealth);
        knockTime = startKnockTime;
    }

    // Update is called once per frame
    void Update()
    {
        rb = GetComponent<Rigidbody2D>();
        if (knockingBack == true)
        {
            GameManager.IsInputEnabled = false;
            knockTime -= Time.deltaTime;
            if (knockTime <= 0)
            {
                rb.velocity = Vector2.zero;
                knockingBack = false;
            }
        }
        else if (knockingBack == false)
        {
            GameManager.IsInputEnabled = true;
            knockTime = startKnockTime;
        }
        if(currentHealth <= 0)
        {
            SkullBar.kills = 0;
            SceneManager.LoadScene("Death Screen");
        }
    }
    public void TakeDamage(int damage)
    {
        currentHealth -= damage;
        healthBar.SetHealth(currentHealth);
        if (DashDirection.direction == 2)
        {
            gameObject.GetComponent<Rigidbody2D>().AddForce(new Vector2(-knockBack, 0f), ForceMode2D.Impulse);
            knockingBack = true;
        }
        else if (DashDirection.direction == 1)
        {
            gameObject.GetComponent<Rigidbody2D>().AddForce(new Vector2(knockBack, 0f), ForceMode2D.Impulse);
            knockingBack = true;
        }
    }
    private void OnCollisionEnter2D(Collision2D collision)
    {
        if (collision.collider.tag == "Mob" && playerAttack.attacking == false)
        {
            //print("player hit");
            TakeDamage(2);
        }
        if (collision.gameObject.tag == "Spikes")
        {
            TakeDamage(10);
        }
    }
    void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject.tag == "Death")
        {
            TakeDamage(10);
        }
    }

}
