/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monprojet.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import monprojet.dao.CityRepository;
import monprojet.dao.CountryRepository;
import monprojet.entity.City;
import monprojet.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Utilisateur
 */
@Controller // This means that this class is a Controller
@RequestMapping(path = "/cities") // This means URL's start with /cities (after Application path)
@Slf4j
public class CityController {

    // On affichera par défaut la page 'cities.html'
    private static final String DEFAULT_VIEW = "city";
    private static final String UPDATE_VIEW = "city";

    //On injecte le repository dans le controlleur
    @Autowired
    private CityRepository cityDao;

    @Autowired
    private CountryRepository countryDao;

    /**
     * Affiche la page d'édition des villes
     *
     * @param model Les infos transmises à la vue (injecté par Spring)
     * @return le nom de la vue à afficher
     */
    @GetMapping(path = "show") //à l'URL http://localhost:8989/cities/show
    public String montreLesVilles(Model model) {
        log.info("On affiche les villes");
        // On initialise la ville avec des valeurs par défaut
        Country france = countryDao.findById(1).orElseThrow();
        City nouvelle = new City("Nouvelle ville", france);
        nouvelle.setPopulation(50);
        model.addAttribute("cities", cityDao.findAll());
        model.addAttribute("city", nouvelle);
        model.addAttribute("countries", countryDao.findAll());
        return DEFAULT_VIEW;
    }
    
    @GetMapping(path="update") // Requête HTTP POST à l'URL http://localhost:8989/cities/update
    public String ModifierVille(@RequestParam("id")City laVille,Model model) {
        model.addAttribute("country", countryDao.findAll());
        model.addAttribute("city",laVille);
        return UPDATE_VIEW;
    }
    
    @GetMapping(path="delete") // Requête HTTP POST à l'URL http://localhost:8989/cities/delete
    public String SupprimerVille(@RequestParam("id")City laVille, Model model){
       cityDao.delete(laVille);
       return "redirect:/cities/show";
    }
   
    /**
	 * Insère une nouvelle ville dans la base
	 * @param laVille la ville à insérer, initialisée par Spring à partir des valeurs soumises dans le formulaire
	 * Spring fera automatiquement une requête SQL SELECT pour récupérer le pays à partir de son id.	 
	 * Spring fera une requête SQL INSERT pour insérer la ville dans la base
	 * @return
	 */
    @PostMapping(path = "save") // Requête HTTP POST à l'URL http://localhost:8989/cities/save
    public String enregistreUneVille(City laVille) {
        cityDao.save(laVille);
        log.info("La ville {} a été enregistrée", laVille);
        // On redirige vers la page de liste des villes
        return "redirect:/cities/show";
    }
}
