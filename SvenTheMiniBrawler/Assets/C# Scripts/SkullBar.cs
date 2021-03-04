using System.Collections;
using System.Collections.Generic;
using System.Diagnostics.Tracing;
using UnityEngine;
using UnityEngine.UI;

public class SkullBar : MonoBehaviour
{
    public Slider slider;
    public static int kills = 0;
    public int maxKills = 10;
    // Start is called before the first frame update
    public void SetMaxKillCount(int kills)
    {
        slider.maxValue = kills;
        slider.value = 0;
    }
    public void SetKillCount(int kills)
    {
        slider.value = kills;
    }
    public void Start()
    {
        SetKillCount(kills);
        SetMaxKillCount(maxKills);
    }
    public void Update()
    {
        SetKillCount(kills);
    }
}
