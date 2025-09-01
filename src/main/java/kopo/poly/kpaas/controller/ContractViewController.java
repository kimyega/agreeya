package kopo.poly.kpaas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/contract")
public class ContractViewController {

    @GetMapping("/upload")  public String upload()  { return "contract/upload"; }
    @GetMapping("/loading") public String loading() { return "contract/loading"; }
    @GetMapping("/result")  public String result()  { return "contract/result"; }
    @GetMapping("/similar") public String similar() { return "contract/similarCase"; }
    @GetMapping("/country") public String country() { return "contract/country"; }
    @GetMapping("/draft")   public String draft()   { return "contract/aiContract"; }
}
