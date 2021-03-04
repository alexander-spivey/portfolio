using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraFollow : MonoBehaviour
{
    public Vector2 velocity;
    public Transform Player;
    public Move2D move2D;

    public float z = -10f;
    public int maxY;
    public int minY;

    public int maxX;
    public int minX;

    public float smoothTimeX;
    public float smoothTimeY;
    void Start()
    {
        
    }

    // Update is called once per frame
    void FixedUpdate()
    {
        Vector3 cameraPositionY = transform.position;
        if(move2D.isJumping == true)
        {
            smoothTimeY = 0f;
        }
        else if (move2D.isJumping == false)
        {
            smoothTimeY = 0.75f;
        }
        cameraPositionY.y = Mathf.SmoothDamp(transform.position.y, Player.transform.position.y, ref velocity.y, smoothTimeY);
        if(cameraPositionY.y < minY)
        {
            cameraPositionY.y = minY;
        }
        if(cameraPositionY.y > maxY)
        {
            cameraPositionY.y = maxY;
        }
        transform.position = cameraPositionY;
    }
    private void Update()
    {
        Vector3 cameraPositionX = transform.position;
        cameraPositionX.x = Mathf.SmoothDamp(transform.position.x, Player.transform.position.x, ref velocity.x, smoothTimeX);
        if (cameraPositionX.x < minX)
        {
            cameraPositionX.x = minX;
        }
        if (cameraPositionX.x > maxX)
        {
            cameraPositionX.x = maxX;
        }
        transform.position = cameraPositionX;
    }
}
