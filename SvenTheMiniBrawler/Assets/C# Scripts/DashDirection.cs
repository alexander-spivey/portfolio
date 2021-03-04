using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class DashDirection : MonoBehaviour
{
    private Rigidbody2D rb;
    public float dashSpeed;
    public float dashTime;
    public float startDashTime;

    public static int direction = 1;

    private bool dashing = false;
    private bool allowedDash = true;

    private bool facingRightBF = true;
    private bool facingRight = true;
    private Animator anim;

    void Start()
    {
        direction = 2;
        anim = GetComponent<Animator>();
        rb = GetComponent<Rigidbody2D>();
        dashTime = startDashTime;
    }

    void Update()
    {
        if(GameManager.IsInputEnabled == true)
        {
            AnimationTurn(); //Model Flipper
            Dash();
        } 
    }

    void AnimationTurn()
    {
        Vector3 characterScale = transform.localScale;
        Vector3 characterPosition = transform.position;
        if (Input.GetKey(KeyCode.LeftArrow) || Input.GetKey(KeyCode.RightArrow))
        {
            if (Input.GetAxis("Horizontal") < 0) //Going Left
            {
                direction = 1;
                facingRightBF = false;
                if (facingRightBF != facingRight)
                {
                    characterScale.x = -1;
                    characterPosition.x -= 1.5f;
                    facingRight = false;
                }
                //characterScale.z = 39.78555f;
                //transform.Rotate(0.0f, 180.0f, 0.0f, Space.Self);
                //characterRotation.y = 180; 
            }
            if (Input.GetAxis("Horizontal") > 0) //Going Right
            {
                direction = 2;
                facingRightBF = true;
                if (facingRightBF != facingRight)
                {
                    characterScale.x = +1;
                    characterPosition.x += 1.5f;
                    facingRight = true;
                }
            }
            transform.position = characterPosition;
            transform.localScale = characterScale;
            anim.SetBool("isRunning", true);
        }
        else
        {
            anim.SetBool("isRunning", false);
        }
    }

    void Dash()
    {
        //Dash Press
        if (Input.GetKeyDown(KeyCode.C) && dashTime > 0 && direction == 2 && allowedDash == true)
        {
            //TRIGGER FOR DASH ANIMATION
            rb.velocity = Vector2.zero;
            gameObject.GetComponent<Rigidbody2D>().AddForce(new Vector2(dashSpeed, 0f), ForceMode2D.Impulse);
            dashing = true;
            allowedDash = false;
        }
        else if (Input.GetKeyDown(KeyCode.C) && dashTime > 0 && direction == 1 && allowedDash == true)
        {
            //TRIGGER FOR DASH ANIMATION
            rb.velocity = Vector2.zero;
            gameObject.GetComponent<Rigidbody2D>().AddForce(new Vector2(-dashSpeed, 0f), ForceMode2D.Impulse);
            dashing = true;
            allowedDash = false;
        }

        //Dashing Check & Timer
        if (dashing == true)
        {
            rb.gravityScale = 0f;
            GameManager.IsInputEnabled = false;
            dashTime -= Time.deltaTime;
            if (dashTime <= 0)
            {
                dashing = false;
                rb.velocity = Vector2.zero;
            }
        }
        else if (dashing == false)
        {
            GameManager.IsInputEnabled = true;
            dashTime = startDashTime;
            rb.gravityScale = 1f;
        }
    }

    void OnCollisionEnter2D(Collision2D col) //CHECK GROUND STATUS
    {
        if (col.collider.tag == "Ground")
        {
            allowedDash = true;
        }
    }
    private void OnCollisionStay2D(Collision2D col)
    {
        if (col.collider.tag == "Ground")
        {
            allowedDash = true;
        }
    }
}
