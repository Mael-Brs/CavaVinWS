import {browser, by, element} from 'protractor';
import {NavBarPage} from './../page-objects/jhi-page-objects';

const path = require('path');

describe('WineInCellar e2e test', () => {

    let navBarPage: NavBarPage;
    let wineInCellarDialogPage: WineInCellarDialogPage;
    let wineInCellarComponentsPage: WineInCellarComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load WineInCellars', () => {
        navBarPage.goToEntity('wine-in-cellar');
        wineInCellarComponentsPage = new WineInCellarComponentsPage();
        expect(wineInCellarComponentsPage.getTitle()).toMatch(/cavavinApp.wineInCellar.home.title/);

    });

    it('should load create WineInCellar dialog', () => {
        wineInCellarComponentsPage.clickOnCreateButton();
        wineInCellarDialogPage = new WineInCellarDialogPage();
        expect(wineInCellarDialogPage.getModalTitle()).toMatch(/cavavinApp.wineInCellar.home.createOrEditLabel/);
        wineInCellarDialogPage.close();
    });

   /* it('should create and save WineInCellars', () => {
        wineInCellarComponentsPage.clickOnCreateButton();
        wineInCellarDialogPage.setPriceInput('5');
        expect(wineInCellarDialogPage.getPriceInput()).toMatch('5');
        wineInCellarDialogPage.setQuantityInput('5');
        expect(wineInCellarDialogPage.getQuantityInput()).toMatch('5');
        wineInCellarDialogPage.setCommentsInput('comments');
        expect(wineInCellarDialogPage.getCommentsInput()).toMatch('comments');
        wineInCellarDialogPage.setLocationInput('location');
        expect(wineInCellarDialogPage.getLocationInput()).toMatch('location');
        wineInCellarDialogPage.setCellarIdInput('5');
        expect(wineInCellarDialogPage.getCellarIdInput()).toMatch('5');
        wineInCellarDialogPage.setChildYearInput('5');
        expect(wineInCellarDialogPage.getChildYearInput()).toMatch('5');
        wineInCellarDialogPage.setApogeeYearInput('5');
        expect(wineInCellarDialogPage.getApogeeYearInput()).toMatch('5');
        wineInCellarDialogPage.vintageSelectLastOption();
        wineInCellarDialogPage.save();
        expect(wineInCellarDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class WineInCellarComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-wine-in-cellar div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class WineInCellarDialogPage {
    modalTitle = element(by.css('h4#myWineInCellarLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    priceInput = element(by.css('input#field_price'));
    quantityInput = element(by.css('input#field_quantity'));
    commentsInput = element(by.css('input#field_comments'));
    locationInput = element(by.css('input#field_location'));
    cellarIdInput = element(by.css('input#field_cellarId'));
    childYearInput = element(by.css('input#field_childYear'));
    apogeeYearInput = element(by.css('input#field_apogeeYear'));
    vintageSelect = element(by.css('select#field_vintage'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setPriceInput = function (price) {
        this.priceInput.sendKeys(price);
    }

    getPriceInput = function () {
        return this.priceInput.getAttribute('value');
    }

    setQuantityInput = function (quantity) {
        this.quantityInput.sendKeys(quantity);
    }

    getQuantityInput = function () {
        return this.quantityInput.getAttribute('value');
    }

    setCommentsInput = function (comments) {
        this.commentsInput.sendKeys(comments);
    }

    getCommentsInput = function () {
        return this.commentsInput.getAttribute('value');
    }

    setLocationInput = function (location) {
        this.locationInput.sendKeys(location);
    }

    getLocationInput = function () {
        return this.locationInput.getAttribute('value');
    }

    setCellarIdInput = function (cellarId) {
        this.cellarIdInput.sendKeys(cellarId);
    }

    getCellarIdInput = function () {
        return this.cellarIdInput.getAttribute('value');
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

    vintageSelectLastOption = function () {
        this.vintageSelect.all(by.tagName('option')).last().click();
    }

    vintageSelectOption = function (option) {
        this.vintageSelect.sendKeys(option);
    }

    getVintageSelect = function () {
        return this.vintageSelect;
    }

    getVintageSelectedOption = function () {
        return this.vintageSelect.element(by.css('option:checked')).getText();
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
