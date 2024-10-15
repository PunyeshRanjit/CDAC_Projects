using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace DestinaFinal.Migrations
{
    /// <inheritdoc />
    public partial class packagelocationupdated : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "location",
                table: "Packages",
                newName: "Location");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Location",
                table: "Packages",
                newName: "location");
        }
    }
}
