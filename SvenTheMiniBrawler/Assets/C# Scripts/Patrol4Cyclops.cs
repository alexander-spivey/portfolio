using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Patrol4Cyclops : MonoBehaviour
{
    public SkullBar skullBar;

    private Rigidbody2D rb;
    private bool knockingBack = false;
    public float knockBack = 7;
    public float knockTime;
    public float startKnockTime;

    public float moveSpeed = 5f;
    public float distance;

    public bool movingRight = true;
    private bool moving = true;
    public float attackTimer = 0f;
    public float startAttackTimer = 0.3f;

    public float attackingTimer = 0f;
    public float startAttackingTimer = 3f;

    public bool hit = false;
    private bool attacking = false;

    public float health = 10;
    public float damage = 3;

    public Collider2D attackTrigger;
    public Transform groundDetection;

    private Animator anim;

    private void Awake()
    {
        anim = gameObject.GetComponent<Animator>();
    }

    private void Start()
    {
        rb = GetComponent<Rigidbody2D>();
        attackTrigger.enabled = false;
        attackTimer = startAttackTimer;
        attackingTimer = startAttackingTimer;
    }

    private void Update()
    {
        if (health <= 0) //KILL DAMAGE KNOCKBACK
        {
            print("DEAD");
            SkullBar.kills++;
            Destroy(gameObject);
            print(SkullBar.kills);
        }
    }

    private void FixedUpdate()
    {
        if (attacking == true) //ATTACK INFO
        {
            if(attackingTimer > 0)
            {
                attackingTimer -= Time.deltaTime;
                attackTimer = startAttackTimer;
            }
            if(attackingTimer < 0)
            {   
                if (attackTimer == startAttackTimer)
                {
                    attackTrigger.enabled = true;
                    anim.SetTrigger("attack");
                    attackTimer -= Time.deltaTime;
                }
                if (attackTimer > 0)
                {
                    attackTimer -= Time.deltaTime;
                }
                if (attackTimer < 0)
                {
                    attackTrigger.enabled = false;
                    attackingTimer = startAttackingTimer;
                    attacking = false;
                    return;
                }
            }
        }

        if (knockingBack == true)
        {
            moving = false;
            knockTime -= Time.deltaTime;
            if (knockTime <= 0)
            {
                rb.velocity = Vector2.zero;
                moving = true;
                knockingBack = false;
            }
        }
        else if (knockingBack == false)
        {
            knockTime = startKnockTime;
        }

        if (moving == true) //MOVE FUNCTIONS
        {
            anim.SetBool("isPatroling", true);
            if (movingRight == true)
            {
                transform.Translate(Vector2.right * moveSpeed * Time.deltaTime);
            }
            else if (movingRight == false)
            {
                transform.Translate(Vector2.left * moveSpeed * Time.deltaTime);
            }
        }
        RaycastHit2D groundInfo = Physics2D.Raycast(groundDetection.position, Vector2.down, distance);
        if (rb.velocity.y == 0)
        {
            if (groundInfo.collider == false) //if it is about to fall off
            {
                Vector3 characterScale = transform.localScale;
                Vector3 characterPosition = transform.position;
                if (movingRight == true)
                {
                    characterScale.x = -1;
                    characterPosition.x -= 1.5f;
                    movingRight = false;
                }
                else
                {
                    characterScale.x = +1;
                    characterPosition.x += 1.5f;
                    movingRight = true;
                }
                transform.position = characterPosition;
                transform.localScale = characterScale;
            }
        }
        else if(rb.velocity.y != 0)
        {
            //if falling basically
        }
    }

    void OnCollisionEnter2D(Collision2D groundInfo) //if it hit walls tag, it changes direction
    {
        if (groundInfo.collider.tag == "Weapon")
        {
            health -= damage;    
            if(DashDirection.direction == 2)
            {
                gameObject.GetComponent<Rigidbody2D>().AddForce(new Vector2(knockBack, 0.1f), ForceMode2D.Impulse);
            }
            else if (DashDirection.direction == 1)
            {
                gameObject.GetComponent<Rigidbody2D>().AddForce(new Vector2(-knockBack, 0.1f), ForceMode2D.Impulse);
            }
            knockingBack = true;
            print("Hit by weapon" + health);
        }

        Vector3 characterScale = transform.localScale;
        Vector3 characterPosition = transform.position;
        if (groundInfo.collider.tag == "Wall")
        {
            print("wall");
            if (movingRight == true)
            {
                characterScale.x = -1;
                characterPosition.x -= 1.5f;
                movingRight = false;
            }
            else
            {
                characterScale.x = +1;
                characterPosition.x += 1.5f;
                movingRight = true;
            }
            transform.position = characterPosition;
            transform.localScale = characterScale;
        }
    }
    void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject.tag == "Player")
        {
            moving = false;
            anim.SetBool("isPatroling", false);
            attacking = true; 
        }
    }
    void OnTriggerExit2D(Collider2D collision)
    {
        if (collision.gameObject.tag == "Player")
        {
            moving = true;
            anim.SetBool("isPatroling", true);
            attacking = false;
        }
        if (collision.gameObject.tag == "Death")
        {
            health -= health;
        }
    }
}
