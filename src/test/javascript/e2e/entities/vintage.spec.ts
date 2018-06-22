import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Vintage e2e test', () => {

    let navBarPage: NavBarPage;
    let vintageDialogPage: VintageDialogPage;
    let vintageComponentsPage: VintageComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Vintages', () => {
        navBarPage.goToEntity('vintage');
        vintageComponentsPage = new VintageComponentsPage();
        expect(vintageComponentsPage.getTitle()).toMatch(/cavavinApp.vintage.home.title/);

    });

    it('should load create Vintage dialog', () => {
        vintageComponentsPage.clickOnCreateButton();
        vintageDialogPage = new VintageDialogPage();
        expect(vintageDialogPage.getModalTitle()).toMatch(/cavavinApp.vintage.home.createOrEditLabel/);
        vintageDialogPage.close();
    });

   /* it('should create and save Vintages', () => {
        vintageComponentsPage.clickOnCreateButton();
        vintageDialogPage.setYearInput('5');
        expect(vintageDialogPage.getYearInput()).toMatch('5');
        vintageDialogPage.setChildYearInput('5');
        expect(vintageDialogPage.getChildYearInput()).toMatch('5');
        vintageDialogPage.setApogeeYearInput('5');
        expect(vintageDialogPage.getApogeeYearInput()).toMatch('5');
        vintageDialogPage.setBareCodeInput('5');
        expect(vintageDialogPage.getBareCodeInput()).toMatch('5');
        vintageDialogPage.wineSelectLastOption();
        vintageDialogPage.save();
        expect(vintageDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class VintageComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-vintage div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class VintageDialogPage {
    modalTitle = element(by.css('h4#myVintageLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    yearInput = element(by.css('input#field_year'));
    childYearInput = element(by.css('input#field_childYear'));
    apogeeYearInput = element(by.css('input#field_apogeeYear'));
    bareCodeInput = element(by.css('input#field_bareCode'));
    wineSelect = element(by.css('select#field_wine'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setYearInput = function (year) {
        this.yearInput.sendKeys(year);
    }

    getYearInput = function () {
        return this.yearInput.getAttribute('value');
    }

    setChildYearInput = function (childYear) {
        this.childYearInput.sendKeys(childYear);
    }

    getChildYearInput = function () {
        return this.childYearInput.getAttribute('value');
    }

    setApogeeYearInput = function (apogeeYear) {
        this.apogeeYearInput.sendKeys(apogeeYear);
    }

    getApogeeYearInput = function () {
        return this.apogeeYearInput.getAttribute('value');
    }

    setBareCodeInput = function (bareCode) {
        this.bareCodeInput.sendKeys(bareCode);
    }

    getBareCodeInput = function () {
        return this.bareCodeInput.getAttribute('value');
    }

    wineSelectLastOption = function () {
        this.wineSelect.all(by.tagName('option')).last().click();
    }

    wineSelectOption = function (option) {
        this.wineSelect.sendKeys(option);
    }

    getWineSelect = function () {
        return this.wineSelect;
    }

    getWineSelectedOption = function () {
        return this.wineSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
