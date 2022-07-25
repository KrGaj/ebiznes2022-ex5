const server = require("./../config/config.json")
const categories = require("./../fixtures/category.json")

describe("POST category", () => {
    it("Add category", ()=> {
        cy.request("POST", server.serverURL + "categories", categories[0])
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("categoryId")

                Cypress.env("categoryId", response.body.categoryId)
            })
    })

    it("Duplicate category", () => {
        cy.request("POST", server.serverURL + "categories", categories[0])
            .then((response) => {
                expect(response.status).to.eq(500)
            })
    })
})

describe("GET category", () => {
    it("GET all categories", () => {
        cy.request("GET", server.serverURL + "categories/all")
            .then((response) => {
                expect(response.status).to.eq(200)
                assert.isNotNull(response.body)
                expect(response.body).to.include(categories[0].name)
            })
    })

    it("GET category by ID", () => {
        cy.request("GET", server.serverURL + "categories?category_id=" + Cypress.env("categoryId"))
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("id")
                expect(response.body).to.have.property("name")

                expect(response.body.id).to.eq(categories[0].name)
            })
    })
})