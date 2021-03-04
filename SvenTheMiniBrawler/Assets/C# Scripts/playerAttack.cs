using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class playerAttack : MonoBehaviour
{
    private Rigidbody2D rb;
    public SkullBar skullBar;

    public static bool attacking = false;
    public float attackTimer = 0;
    public float attackCD = 0.3f;

    public Collider2D attackTrigger;
    private Animator anim;

    private void Awake()
    {
        anim = gameObject.GetComponent<Animator>();
        attackTrigger.enabled = false;
    }
    private void Start()
    {
        rb = GetComponent<Rigidbody2D>();
        attackTrigger.enabled = false;
    }

    private void Update()
    {
        if(attackTimer > 0)
        {
            attackTimer -= Time.deltaTime;
            //print("Attacking: " + attackTimer + attacking);
        }
        if(attackTimer < 0)
        {
            attackTrigger.enabled = false;
            attacking = false;
            //print("Attacking: " + attackTimer + attacking);
            attackTimer = 0;
            //print(attackTrigger.enabled);
        }
        if (Input.GetKeyDown(KeyCode.X) && attacking == false && attackTimer == 0)
        {
            attacking = true;
            //print("Attacking: " + attacking);
            attackTrigger.enabled = true;
            anim.SetTrigger("attack");
            attackTimer = attackCD;
            //print(attackTrigger.enabled);
        }
    }
}
