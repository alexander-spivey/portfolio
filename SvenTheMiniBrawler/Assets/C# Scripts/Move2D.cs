using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Move2D : MonoBehaviour
{
    private Rigidbody2D rb;
    public float moveSpeed = 5f;
    public float jumpHeight = 10f;
    public float jumpCounter = 0f;
    public float jumpsAllowed = 2f;
    
    public bool isJumping = false;

    private Animator anim;

    private void Awake()
    {
        anim = gameObject.GetComponent<Animator>();
    }

    // Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody2D>();
        isJumping = true;
    }

    // Update is called once per frame
    void Update()
    {
        if(GameManager.IsInputEnabled)
        {
            Jump();
            Move();
        }
    }

    void Move() //left right move
    {
        Vector3 movement = new Vector3(Input.GetAxis("Horizontal"), 0f, 0f);
        transform.position += movement * Time.deltaTime * moveSpeed;
    }


    void Jump() //up move && jump counters
    {
        if (Input.GetKeyDown(KeyCode.Z) && jumpCounter > 0)
        {
            //if (rb.velocity.y < -0.1f)
            rb.velocity = Vector2.zero; //Set velocity to zero so stops all fall and cant chain jump height
            gameObject.GetComponent<Rigidbody2D>().AddForce(new Vector2(0f, jumpHeight), ForceMode2D.Impulse);
            isJumping = true;
            anim.SetTrigger("jump");
            jumpCounter--;
        }
        if (isJumping == false)
        {
            jumpCounter = jumpsAllowed;
        }
    }
    void OnCollisionEnter2D(Collision2D col)
    {
        if(col.collider.tag == "Ground")
        {
            isJumping = false;
        }
    }
    void OnCollisionExit2D(Collision2D col)
    {
        if (col.collider.tag == "Ground")
        {
            isJumping = true;
        }
    }
}
